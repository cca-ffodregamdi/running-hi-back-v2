package com.runninghi.runninghibackv2.application.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.runninghi.runninghibackv2.application.dto.alarm.request.CreateAlarmRequest;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.exception.custom.FcmException;
import com.runninghi.runninghibackv2.common.exception.custom.KakaoOauthProfileException;
import com.runninghi.runninghibackv2.common.exception.custom.KakaoOauthUnlinkException;
import com.runninghi.runninghibackv2.domain.entity.vo.RunDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.AlarmType;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.common.exception.custom.KakaoOauthException;
import com.runninghi.runninghibackv2.application.dto.member.response.KakaoProfileResponse;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.enumtype.TargetPage;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOauthService {

    private static final int NICKNAME_DIGIT_LENGTH = 5;
    private static final int RANDOM_NUMBER_RANGE = 10;
    private static final String KAKAO_USER_INFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String KAKAO_UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";
    private static final String SIGNUP_FCM_TITLE = "환영합니다! 러닝을 시작하여 다음 레벨에 도전하세요!";

    @Value("${kakao.admin-key}")
    private String adminKey;

    @Value("${cloud.aws.s3.default-profile}")
    private String defaultProfileImageUrl;

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AlarmService alarmService;

    private final SecureRandom random = new SecureRandom();


    /**
     * 카카오 토큰을 사용하여 검증하고, 검증된 사용자의 정보를 반환합니다.
     * @param kakaoToken 카카오로부터 받은 인가 코드
     * @return 인증된 사용자의 액세스 토큰 및 리프레시 토큰
     * @throws KakaoOauthException 카카오 로그인 처리 중 오류가 발생한 경우 예외가 발생합니다.
     */
    @Transactional
    public Map<String, String> kakaoOauth(String kakaoToken) {
        try {
            KakaoProfileResponse kakaoProfileResponse = getKakaoProfile(kakaoToken);
            if (kakaoProfileResponse == null) {
                log.error("카카오 프로필 정보를 가져오는 데 실패했습니다.");
                throw new KakaoOauthProfileException();
            }
            log.info("카카오 프로필 정보를 성공적으로 가져왔습니다. 프로필: {}", kakaoProfileResponse);

            Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoProfileResponse.getKakaoId().toString());

            return optionalMember.map(this::loginWithKakao).orElseGet(() -> {
                try {
                    return loginWithKakaoCreateMember(kakaoProfileResponse);
                } catch (FirebaseMessagingException e) {
                    throw new FcmException(e.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("카카오 OAuth 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new KakaoOauthException();
        }
    }

    /**
     * 카카오 사용자의 프로필 정보를 가져옵니다.
     *
     * @param kakaoAccessToken 카카오 액세스 토큰
     * @return 카카오 사용자의 프로필 정보
     */
    private KakaoProfileResponse getKakaoProfile(String kakaoAccessToken){
        log.info("카카오 프로필 정보를 요청합니다. 액세스 토큰: {}", kakaoAccessToken);

        // http header 설정 : access token 을 넣어서 user 정보에 접근할 수 있도록 한다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        // 필요한 사용자 정보 가져오기
        MultiValueMapAdapter<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"id\", \"properties.nickname\"]");

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        KakaoProfileResponse response = restTemplate.postForObject(KAKAO_USER_INFO_REQUEST_URL, request, KakaoProfileResponse.class);
        log.info("카카오 프로필 정보를 성공적으로 가져왔습니다. 응답: {}", response);

        return response;    }

    private Map<String, String> generateTokens(Member member, boolean isNewMember) {
        log.info("토큰 생성 시작. 회원 번호: {}, 새 회원 여부: {}", member.getMemberNo(), isNewMember);

        AccessTokenInfo accessTokenInfo = new AccessTokenInfo(member.getMemberNo(), member.getRole());
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo(member.getKakaoId(), member.getRole());

        String refreshToken = jwtTokenProvider.createRefreshToken(refreshTokenInfo);
        String accessToken = jwtTokenProvider.createAccessToken(accessTokenInfo);

        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("memberNo", member.getMemberNo().toString());

        log.info("새 회원 생성 후 멤버 번호 추가: {}", member.getMemberNo());

        return response;
    }

    // 로그인 메서드
    private Map<String, String> loginWithKakao(Member member) {
        log.info("카카오 로그인을 수행합니다. 회원 번호: {}", member.getMemberNo());

        member.activateMember();  // 멤버의 활성화 상태를 true로 변경, deactivateDate를 null로 설정

        return generateTokens(member, false);
    }

    // 회원 생성 및 로그인 메서드
    private Map<String, String> loginWithKakaoCreateMember(KakaoProfileResponse kakaoProfile) throws FirebaseMessagingException {
        log.info("새 회원을 생성하고 로그인합니다. 프로필: {}", kakaoProfile);

        Member member = Member.builder()
                .kakaoId(kakaoProfile.getKakaoId().toString())
                .name(kakaoProfile.getNickname())
                .profileImageUrl(defaultProfileImageUrl)
                .nickname("러너 " + generateRandomDigits())
                .isActive(true)
                .isBlacklisted(false)
                .isTermsAgreed(false)
                .role(Role.USER)
                .runDataVO(new RunDataVO(0.0,0.0,2,0))
                .build();

        memberRepository.saveAndFlush(member);
        log.info("새 회원을 성공적으로 저장했습니다. 회원 번호: {}", member.getMemberNo());

        // 회원가입한 사용자에게 알림
        CreateAlarmRequest alarmRequest = CreateAlarmRequest.builder()
                .title(SIGNUP_FCM_TITLE)
                .targetMemberNo(member.getMemberNo())
                .alarmType(AlarmType.LEVEL_UP)
                .targetPage(TargetPage.MYPAGE)
                .targetId(member.getMemberNo())
                .fcmToken(member.getFcmToken())
                .build();
        alarmService.createPushAlarm(alarmRequest);

        return generateTokens(member, true);
    }

    /**
     * 회원의 카카오 계정을 연결 해제하고 회원을 삭제합니다.
     *
     * @param memberNo 회원 번호
     * @return 회원의 활성화 상태를 토글한 결과 (활성화되었으면 true, 비활성화되었으면 false)
     * @throws EntityNotFoundException 요청된 회원 번호에 해당하는 회원을 찾을 수 없을 때 발생하는 예외
     * @throws KakaoOauthUnlinkException 카카오 API 호출이 실패한 경우 발생하는 예외
     */
    @Transactional
    public boolean unlinkAndDeleteMember(Long memberNo) {
        log.info("회원의 카카오 계정 연결 해제 요청을 받았습니다. 회원 번호: {}", memberNo);

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> {
                    log.error("회원 번호 {}에 해당하는 회원을 찾을 수 없습니다.", memberNo);
                    return new EntityNotFoundException();
                });

        // http header 설정 : access token 을 넣어서 user 정보에 접근할 수 있도록 한다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", member.getKakaoId());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =  restTemplate.exchange(KAKAO_UNLINK_URL, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("카카오 계정 연결 해제 요청이 실패했습니다. 응답 상태: {}", response.getStatusCode());
            throw new KakaoOauthUnlinkException();
        }

        // 멤버의 활성화 상태를 false로 변경, deactivateDate 설정
        member.deactivateMember();
        memberRepository.save(member);
        log.info("회원의 카카오 계정 연결 해제 및 삭제 완료. 회원 번호: {}", memberNo);

        return member.isActive();
    }

    /**
     * 무작위 숫자로 구성된 문자열을 생성하여 반환합니다.
     *
     * @return 생성된 무작위 숫자 8자리 문자열
     */
    private String generateRandomDigits() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < NICKNAME_DIGIT_LENGTH; i++) {
            stringBuilder.append(random.nextInt(RANDOM_NUMBER_RANGE));
        }

        String randomDigits = stringBuilder.toString();
        log.debug("무작위 숫자 생성: {}", randomDigits);

        return randomDigits;
    }

}
