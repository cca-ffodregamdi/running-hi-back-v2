package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.member.request.AdminSignInRequest;
import com.runninghi.runninghibackv2.application.dto.member.request.AdminSignUpRequest;
import com.runninghi.runninghibackv2.application.dto.member.request.UpdateCurrentLocationRequest;
import com.runninghi.runninghibackv2.application.dto.member.request.UpdateMemberInfoRequest;
import com.runninghi.runninghibackv2.application.dto.member.response.GetMemberResponse;
import com.runninghi.runninghibackv2.application.dto.member.response.UpdateCurrentLocationResponse;
import com.runninghi.runninghibackv2.application.dto.member.response.UpdateMemberInfoResponse;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.exception.custom.AdminLoginException;
import com.runninghi.runninghibackv2.common.utils.PasswordUtils;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.service.MemberChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberChecker memberChecker;

    private static final String MEMBER_NOT_FOUND_MESSAGE = "회원 정보를 찾을 수 없습니다.";
    private static final String INVALID_NICKNAME_MESSAGE = "유효하지 않은 닉네임입니다.";
    private static final String MEMBER_NOT_FOUND_DETAIL_MESSAGE = "회원 번호 %d에 해당하는 회원을 찾을 수 없습니다.";
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${admin.invitation-code}")
    private String adminInvitationCode;

    private Member findMemberByNoWithLogging(Long memberNo) {
        log.info("회원 조회 요청. 회원 번호: {}", memberNo);
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> {
                    log.error(MEMBER_NOT_FOUND_DETAIL_MESSAGE, memberNo);
                    return new EntityNotFoundException(MEMBER_NOT_FOUND_MESSAGE);
                });
    }

    @Transactional
    public UpdateMemberInfoResponse updateMemberInfo(Long memberNo, UpdateMemberInfoRequest request) throws BadRequestException {
        log.info("회원 정보 업데이트 요청. 회원 번호: {}, 요청 정보: {}", memberNo, request);
        Member member = findMemberByNoWithLogging(memberNo);

        try {
            memberChecker.checkNicknameValidation(request.nickname());
        } catch (IllegalArgumentException e) {
            log.error("닉네임 검증 실패: {}", e.getMessage());
            throw new BadRequestException(INVALID_NICKNAME_MESSAGE);
        }

        member.updateNickname(request.nickname());
        Member updatedMember = memberRepository.save(member);

        log.info("회원 정보 업데이트 성공. 새로운 닉네임: {}", updatedMember.getNickname());
        return UpdateMemberInfoResponse.from(updatedMember.getNickname());
    }

    @Transactional(readOnly = true)
    public GetMemberResponse getMemberInfo(Long memberNo) {
        log.info("회원 정보 조회 요청. 회원 번호: {}", memberNo);
        Member member = findMemberByNoWithLogging(memberNo);

        log.info("회원 정보 조회 성공. 회원 번호: {}", memberNo);
        return GetMemberResponse.from(member);
    }

    public void addReportedCount(Long memberNo) {
        log.info("신고 수 추가 요청. 회원 번호: {}", memberNo);
        Member member = findMemberByNoWithLogging(memberNo);

        member.addReportedCount();
        log.info("신고 수 추가 완료. 회원 번호: {}", memberNo);
    }

    public void saveFCMToken(Long memberNo, String fcmToken, boolean alarmConsent) {
        log.info("FCM 토큰 저장 요청. 회원 번호: {}, FCM 토큰: {}, 알림 동의: {}", memberNo, fcmToken, alarmConsent);
        Member member = findMemberByNoWithLogging(memberNo);

        member.updateFCMToken(fcmToken);
        member.updateAlarmConsent(alarmConsent);

        log.info("FCM 토큰 저장 완료. 회원 번호: {}", memberNo);
    }

    @Transactional
    public UpdateCurrentLocationResponse updateCurrentLocation(Long memberNo, UpdateCurrentLocationRequest request) {
        log.info("위치 정보 업데이트 요청. 회원 번호: {}, 좌표: ({}, {})", memberNo, request.latitude(), request.longitude());

        Member member = findMemberByNoWithLogging(memberNo);

        GeometryFactory gf = new GeometryFactory();
        Point geometry = gf.createPoint(new Coordinate(request.longitude(), request.latitude())); // longitude 경도 == x, latitude 위도 == y
        geometry.setSRID(4326);

        member.updateGeometry(geometry);
        memberRepository.save(member);

        log.info("위치 정보 업데이트 완료. 회원 번호: {}, 좌표: ({}, {})", memberNo, geometry.getX(), geometry.getY());

        return UpdateCurrentLocationResponse.from(memberNo, geometry);
    }

    @Transactional
    public Map<String, String> signinAdmin(AdminSignInRequest request) throws RuntimeException {
        log.info("관리자 로그인 시도: 사용자 account = {}", request.account());

        Member member = memberRepository.findByAccount(request.account())
                .orElseThrow(() -> {
                    log.warn("관리자 로그인 실패: 잘못된 관리자 account = {}", request.account());
                    return new RuntimeException("관리자 account가 잘못되었습니다.");
                });

        if (!PasswordUtils.checkPassword(request.password(), member.getPassword())) {
            log.warn("관리자 로그인 실패: 잘못된 비밀번호. 사용자 account = {}", request.account());
            throw new AdminLoginException("관리자 비밀번호가 잘못되었습니다.");
        }

        if (member.getRole() != Role.ADMIN) {
            log.warn("관리자 로그인 실패: 권한 부족. 사용자 account = {}", request.account());
            throw new AdminLoginException("관리자 권한이 필요합니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(new AccessTokenInfo(member.getMemberNo(), member.getRole()));
        String refreshToken = jwtTokenProvider.createRefreshToken(new RefreshTokenInfo(member.getMemberNo().toString(), member.getRole()));

        log.info("관리자 로그인 성공: 사용자 account = {}", request.account());
        log.debug("발급된 액세스 토큰: {}", accessToken);
        log.debug("발급된 리프레시 토큰: {}", refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    @Transactional
    public void signupAdmin(AdminSignUpRequest request) {
        if (memberRepository.findByAccount(request.account()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 account입니다.");
        }

        Role role = Role.ADMIN;
        if (request.invitationCode() != null && request.invitationCode().equals(adminInvitationCode)) {
            role = Role.ADMIN;
        } else if (request.invitationCode() != null) {
            throw new IllegalArgumentException("잘못된 초대 코드입니다.");
        }

        Member newMember = Member.builder()
                .account(request.account())
                .password(PasswordUtils.hashPassword(request.password()))
                .role(role)
                .isActive(true)
                .build();

        memberRepository.save(newMember);
    }
}
