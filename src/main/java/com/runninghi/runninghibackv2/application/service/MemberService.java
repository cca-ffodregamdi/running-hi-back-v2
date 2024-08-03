package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.member.request.UpdateCurrentLocationRequest;
import com.runninghi.runninghibackv2.application.dto.member.request.UpdateMemberInfoRequest;
import com.runninghi.runninghibackv2.application.dto.member.response.GetMemberResponse;
import com.runninghi.runninghibackv2.application.dto.member.response.UpdateCurrentLocationResponse;
import com.runninghi.runninghibackv2.application.dto.member.response.UpdateMemberInfoResponse;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.service.MemberChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberChecker memberChecker;

    private static final String MEMBER_NOT_FOUND_MESSAGE = "회원 정보를 찾을 수 없습니다.";
    private static final String INVALID_NICKNAME_MESSAGE = "유효하지 않은 닉네임입니다.";
    private static final String MEMBER_NOT_FOUND_DETAIL_MESSAGE = "회원 번호 %d에 해당하는 회원을 찾을 수 없습니다.";

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

    public UpdateCurrentLocationResponse updateCurrentLocation(Long memberNo, UpdateCurrentLocationRequest request) {
        log.info("위치 정보 업데이트 요청. 회원 번호: {}, 좌표: ({}, {})", memberNo, request.x(), request.y());

        Member member = findMemberByNoWithLogging(memberNo);

        GeometryFactory gf = new GeometryFactory();
        Point geometry = gf.createPoint(new Coordinate(request.x(), request.y()));

        member.updateGeometry(geometry);
        memberRepository.save(member);

        log.info("위치 정보 업데이트 완료. 회원 번호: {}, 좌표: ({}, {})", memberNo, geometry.getX(), geometry.getY());

        return UpdateCurrentLocationResponse.from(memberNo, geometry);
    }

}
