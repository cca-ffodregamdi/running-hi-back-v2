package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.alarm.request.CreateAlarmRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.response.*;
import com.runninghi.runninghibackv2.common.exception.custom.FeedbackInvalidDataException;
import com.runninghi.runninghibackv2.domain.entity.Feedback;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.enumtype.AlarmType;
import com.runninghi.runninghibackv2.domain.enumtype.TargetPage;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.FeedbackRepository;
import com.runninghi.runninghibackv2.domain.service.FeedbackChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackChecker feedbackChecker;
    private final AlarmService alarmService;

    private static final String FEEDBACK_REPLY_FCM_TITLE = "회원님의 피드백/문의사항에 답변이 등록되었습니다.";

    @Transactional
    public CreateFeedbackResponse createFeedback(CreateFeedbackRequest request, Long memberNo) {
        log.info("피드백 생성 시도. 회원 번호: {}", memberNo);

        try {
            Member member = findMemberByNo(memberNo);
            feedbackChecker.checkFeedbackValidation(request.title(), request.content());

            Feedback feedback = Feedback.builder()
                    .feedbackWriter(member)
                    .title(request.title())
                    .content(request.content())
                    .category(request.category())
                    .hasReply(false)
                    .build();

            Feedback savedFeedback = feedbackRepository.save(feedback);
            log.info("피드백 생성 성공. 피드백 번호: {}", savedFeedback.getFeedbackNo());

            return CreateFeedbackResponse.from(feedback);
        } catch (BadRequestException e) {
            log.error("피드백 생성 실패. 회원 번호: {}, 유효하지 않은 데이터: {}", memberNo, request.content());
            throw new FeedbackInvalidDataException();
        }
    }

    @Transactional
    public UpdateFeedbackResponse updateFeedback(UpdateFeedbackRequest request, Long feedbackNo, Long memberNo) {
        log.info("피드백 수정 시도. 피드백 번호: {}, 회원 번호: {}", feedbackNo, memberNo);

        try {
            Member member = findMemberByNo(memberNo);
            Feedback feedback = findFeedbackByNo(feedbackNo);

            feedbackChecker.isWriter(member.getMemberNo(), feedback.getFeedbackWriter().getMemberNo());
            feedbackChecker.checkReplyStatus(feedback.isHasReply());
            feedbackChecker.checkFeedbackValidation(request.title(), request.content());

            Feedback updatedFeedback = Feedback.builder()
                    .feedbackNo(feedbackNo)
                    .feedbackWriter(feedback.getFeedbackWriter())
                    .title(request.title())
                    .content(request.content())
                    .category(request.category())
                    .hasReply(false)
                    .build();

            feedbackRepository.save(updatedFeedback);
            log.info("피드백 수정 성공. 피드백 번호: {}", feedbackNo);

            return UpdateFeedbackResponse.from(updatedFeedback);
        } catch (BadRequestException e) {
            log.error("피드백 수정 실패. 피드백 번호: {}, 피드백 수정 중 유효하지 않은 데이터: {}", feedbackNo, request.content());
            throw new FeedbackInvalidDataException();
        }
    }

    @Transactional
    public DeleteFeedbackResponse deleteFeedback(Long feedbackNo, Long memberNo) {
        log.info("피드백 삭제 시도. 피드백 번호: {}, 회원 번호: {}", feedbackNo, memberNo);
        Member member = findMemberByNo(memberNo);
        Feedback feedback = findFeedbackByNo(feedbackNo);

        feedbackChecker.isWriter(member.getMemberNo(), feedback.getFeedbackWriter().getMemberNo());

        feedbackRepository.delete(feedback);
        log.info("피드백 삭제 성공. 피드백 번호: {}", feedbackNo);

        return DeleteFeedbackResponse.from(feedbackNo);

    }

    @Transactional(readOnly = true)
    public GetFeedbackResponse getFeedback(Long feedbackNo, Long memberNo) {
        log.info("피드백 조회 시도. 피드백 번호: {}, 회원 번호: {}", feedbackNo, memberNo);
        Feedback feedback = findFeedbackByNo(feedbackNo);

        feedbackChecker.isWriter(memberNo, feedback.getFeedbackWriter().getMemberNo());
        log.info("피드백 조회 성공. 피드백 번호: {}", feedbackNo);

        return GetFeedbackResponse.from(feedback);
    }

    @Transactional(readOnly = true)
    public GetFeedbackResponse getFeedbackByAdmin(Long feedbackNo, Long memberNo) {
        log.info("관리자 피드백 조회 시도. 피드백 번호: {}, 관리자 번호: {}", feedbackNo, memberNo);
        Member member = findMemberByNo(memberNo);
        feedbackChecker.isAdmin(member.getRole());

        Feedback feedback = findFeedbackByNo(feedbackNo);
        log.info("관리자 피드백 조회 성공. 피드백 번호: {}", feedbackNo);

        return GetFeedbackResponse.from(feedback);

    }

    @Transactional(readOnly = true)
    public FeedbackPageResponse<GetFeedbackResponse> getFeedbackScroll(Pageable pageable, Long memberNo) {
        log.info("회원 피드백 목록 조회 시도. 회원 번호: {}, 페이지: {}", memberNo, pageable.getPageNumber());
        Member member = findMemberByNo(memberNo);

        Page<Feedback> feedbackPage = feedbackRepository.findAllByFeedbackWriter(member, pageable);

        Page<GetFeedbackResponse> response = feedbackPage.map(GetFeedbackResponse::from);
        log.info("회원 피드백 목록 조회 성공. 회원 번호: {}", memberNo);

        return FeedbackPageResponse.from(response);
    }

    @Transactional(readOnly = true)
    public FeedbackPageResponse<GetFeedbackResponse> getFeedbackScrollByAdmin(Pageable pageable, Long memberNo) {
        log.info("관리자 전체 피드백 목록 조회 시도. 관리자 번호: {}, 페이지: {}", memberNo, pageable.getPageNumber());
        Member member = findMemberByNo(memberNo);

        feedbackChecker.isAdmin(member.getRole());

        Page<Feedback> feedbackPage = feedbackRepository.findAllBy(pageable);

        Page<GetFeedbackResponse> response = feedbackPage.map(GetFeedbackResponse::from);
        log.info("관리자 전체 피드백 목록 조회 성공.");

        return FeedbackPageResponse.from(response);
    }

    @Transactional
    public UpdateFeedbackReplyResponse updateFeedbackReply(UpdateFeedbackReplyRequest request, Long feedbackNo, Long memberNo) {
        log.info("피드백 답변 작성/수정 시도. 피드백 번호: {}, 관리자 번호: {}", feedbackNo, memberNo);

        try {
            Member member = findMemberByNo(memberNo);
            feedbackChecker.isAdmin(member.getRole());
            feedbackChecker.checkFeedbackReplyValidation(request.content());

            Feedback feedback = findFeedbackByNo(feedbackNo);

            Feedback updatedFeedback = Feedback.builder()
                    .feedbackNo(feedbackNo)
                    .feedbackWriter(feedback.getFeedbackWriter())
                    .title(feedback.getTitle())
                    .content(feedback.getContent())
                    .category(feedback.getCategory())
                    .hasReply(true)
                    .reply(request.content())
                    .build();

            feedbackRepository.save(updatedFeedback);

            CreateAlarmRequest alarmRequest = CreateAlarmRequest.builder()
                    .title(FEEDBACK_REPLY_FCM_TITLE)
                    .targetMemberNo(updatedFeedback.getFeedbackNo())
                    .alarmType(AlarmType.FEEDBACK)
                    .targetPage(TargetPage.FEEDBACK)
                    .targetId(updatedFeedback.getFeedbackNo())
                    .fcmToken(member.getFcmToken())
                    .build();
            alarmService.createPushAlarm(alarmRequest);
            log.info("피드백 답변 작성/수정 성공. 피드백 번호: {}", feedbackNo);

            return UpdateFeedbackReplyResponse.from(updatedFeedback);
        } catch (BadRequestException e){
            log.error("피드백 답변 작성/수정 중 유효하지 않은 데이터: {}", request, e);
            throw new FeedbackInvalidDataException();
        }
    }

    @Transactional(readOnly = true)
    public FeedbackPageResponse<GetFeedbackResponse> getFeedbackScrollByAdminWithReply(Pageable pageable, Long memberNo, Boolean hasReply) {
        log.info("관리자 답변 상태별 피드백 목록 조회 시도. 관리자 번호: {}, 답변 상태: {}, 페이지: {}",
                memberNo, hasReply, pageable.getPageNumber());

        Member member = findMemberByNo(memberNo);

        feedbackChecker.isAdmin(member.getRole());

        Page<Feedback> feedbackPage = feedbackRepository.findByHasReply(hasReply, pageable);

        Page<GetFeedbackResponse> response = feedbackPage.map(GetFeedbackResponse::from);
        log.info("관리자 답변 상태별 피드백 목록 조회 성공. 답변 상태: {}, 총 피드백 수: {}", hasReply, feedbackPage.getTotalElements());

        return FeedbackPageResponse.from(response);
    }

    private Member findMemberByNo(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> {
                    log.error("해당 번호의 회원을 찾을 수 없음. 회원 번호: {}", memberNo);
                    return new EntityNotFoundException();
                });
    }

    private Feedback findFeedbackByNo(Long feedbackNo) {
        return feedbackRepository.findById(feedbackNo)
                .orElseThrow(() -> {
                    log.error("해당 번호의 피드백을 찾을 수 없음. 피드백 번호: {}", feedbackNo);
                    return new EntityNotFoundException();
                });

    }
}
