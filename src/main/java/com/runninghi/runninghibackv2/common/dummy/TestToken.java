package com.runninghi.runninghibackv2.common.dummy;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class TestToken {

    private final JwtTokenProvider jwtTokenProvider;
    private final TestMemberRepository testMemberRepository;

    @PostMapping("/test/token")
    public ResponseEntity<ApiResult<TestTokenResponse>> getTokens() {
        try {
            List<Member> members = new ArrayList<>();

            Member member1 = Member.builder()
                    .alarmConsent(true)
                    .kakaoId("12345")
                    .kakaoName("관리자 : 카카오 이름")
                    .nickname("관리자 : 테스트용 관리자입니다.")
                    .level(0)
                    .isActive(true)
                    .totalDistance(0)
                    .distanceToNextLevel(10)
                    .role(Role.ADMIN)
                    .build();
            members.add(member1);

            Member member2 = Member.builder()
                    .alarmConsent(true)
                    .kakaoId("67890")
                    .kakaoName("유저 : 카카오 이름")
                    .nickname("유저 : 테스트용 유저입니다.")
                    .level(1)
                    .isActive(true)
                    .totalDistance(0)
                    .distanceToNextLevel(10)
                    .role(Role.USER)
                    .build();
            members.add(member2);

            saveAllAndFlushMembers(members);

            Member admin = testMemberRepository.findByNickname(member1.getNickname());
            Member user = testMemberRepository.findByNickname(member2.getNickname());

            TestTokenResponse response = createTokens(admin, user);

            return ResponseEntity.ok(ApiResult.success("test용 유저, 어드민 생성 성공. 토큰 발급 성공", response));
        } catch (IncorrectResultSizeDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.error(HttpStatus.BAD_REQUEST, "이미 테스트 유저가 존재합니다. 토큰 재발급으로 요청해주세요."));
        }

    }

    @PostMapping("test/re-token")
    public ResponseEntity<ApiResult<TestTokenResponse>> getReTokens() {
        try {
            Member admin = testMemberRepository.findByNickname("관리자 : 테스트용 관리자입니다.");
            Member user = testMemberRepository.findByNickname("유저 : 테스트용 유저입니다.");

            TestTokenResponse response = createTokens(admin, user);

            return ResponseEntity.ok(ApiResult.success("test 토큰 재발급 성공", response));
        } catch (NoSuchElementException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.error(HttpStatus.NOT_FOUND, "먼저 /test/token 으로 요청을 보내 테스트용 유저를 생성해주세요."));
        }
    }

    private TestTokenResponse createTokens(Member admin, Member user) {
        List<Member> members = new ArrayList<>();

        members.add(admin);
        members.add(user);

        AccessTokenInfo adminTokenInfo = AccessTokenInfo.from(admin);
        AccessTokenInfo userTokenInfo = AccessTokenInfo.from(user);

        String adminAccessToken = jwtTokenProvider.createAccessToken(adminTokenInfo);
        String userAccessToken = jwtTokenProvider.createAccessToken(userTokenInfo);

        RefreshTokenInfo adminRefreshTokenInfo = RefreshTokenInfo.from(admin);
        RefreshTokenInfo userRefreshTokenInfo = RefreshTokenInfo.from(user);

        String adminRefreshToken = jwtTokenProvider.createRefreshToken(adminRefreshTokenInfo);
        String userRefreshToken = jwtTokenProvider.createRefreshToken(userRefreshTokenInfo);

        admin.updateRefreshToken(adminRefreshToken);
        user.updateRefreshToken(userRefreshToken);

        saveAllAndFlushMembers(members);

        TokensAndInfo adminTokensAndInfo = TokensAndInfo.from(admin, adminAccessToken, adminRefreshToken);
        TokensAndInfo userTokensAndInfo = TokensAndInfo.from(user, userAccessToken, userRefreshToken);

        return TestTokenResponse.from(adminTokensAndInfo, userTokensAndInfo);
    }


    private void saveAllAndFlushMembers(List<Member> members) {
        for (Member member : members) {
            testMemberRepository.saveAndFlush(member);
        }
    }

}
