package com.runninghi.runninghibackv2.application.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.runninghi.runninghibackv2.application.dto.alarm.request.CreateAlarmRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.response.*;
import com.runninghi.runninghibackv2.domain.entity.Feedback;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.enumtype.AlarmType;
import com.runninghi.runninghibackv2.domain.enumtype.TargetPage;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.FeedbackRepository;
import com.runninghi.runninghibackv2.domain.service.FeedbackChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackChecker feedbackChecker;
    private final AlarmService alarmService;

    private static final String FEEDBACK_REPLY_FCM_TITLE = "회원님의 피드백/문의사항에 답변이 등록되었습니다.";

    @Transactional
    public CreateFeedbackResponse createFeedback(CreateFeedbackRequest request, Long memberNo
    ) throws BadRequestException {

        Member member = findMemberByNo(memberNo);

        feedbackChecker.checkFeedbackValidation(request.title(), request.content());

        Feedback feedback = Feedback.builder()
                .feedbackWriter(member)
                .title(request.title())
                .content(request.content())
                .category(request.category())
                .hasReply(false)
                .build();

        feedbackRepository.save(feedback);

        return CreateFeedbackResponse.from(feedback);
    }

    @Transactional
    public UpdateFeedbackResponse updateFeedback(UpdateFeedbackRequest request, Long feedbackNo, Long memberNo) throws BadRequestException {

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

        return UpdateFeedbackResponse.from(updatedFeedback);
    }

    @Transactional
    public DeleteFeedbackResponse deleteFeedback(Long feedbackNo, Long memberNo) {

        Member member = findMemberByNo(memberNo);
        Feedback feedback = findFeedbackByNo(feedbackNo);

        feedbackChecker.isWriter(member.getMemberNo(), feedback.getFeedbackWriter().getMemberNo());

        feedbackRepository.delete(feedback);

        return DeleteFeedbackResponse.from(feedbackNo);

    }

    @Transactional(readOnly = true)
    public GetFeedbackResponse getFeedback(Long feedbackNo, Long memberNo) {
        Feedback feedback = findFeedbackByNo(feedbackNo);

        feedbackChecker.isWriter(memberNo, feedback.getFeedbackWriter().getMemberNo());

        return GetFeedbackResponse.from(feedback);
    }

    @Transactional(readOnly = true)
    public GetFeedbackResponse getFeedbackByAdmin(Long feedbackNo, Long memberNo) {

        Member member = findMemberByNo(memberNo);
        feedbackChecker.isAdmin(member.getRole());

        Feedback feedback = findFeedbackByNo(feedbackNo);

        return GetFeedbackResponse.from(feedback);

    }

    @Transactional(readOnly = true)
    public FeedbackPageResponse<GetFeedbackResponse> getFeedbackScroll(Pageable pageable, Long memberNo) {

        Member member = findMemberByNo(memberNo);

        Page<Feedback> feedbackPage = feedbackRepository.findAllByFeedbackWriter(member, pageable);

        Page<GetFeedbackResponse> response = feedbackPage.map(GetFeedbackResponse::from);

        return FeedbackPageResponse.from(response);
    }

    @Transactional(readOnly = true)
    public FeedbackPageResponse<GetFeedbackResponse> getFeedbackScrollByAdmin(Pageable pageable, Long memberNo) {
        Member member = findMemberByNo(memberNo);

        feedbackChecker.isAdmin(member.getRole());

        Page<Feedback> feedbackPage = feedbackRepository.findAllBy(pageable);

        Page<GetFeedbackResponse> response = feedbackPage.map(GetFeedbackResponse::from);

        return FeedbackPageResponse.from(response);
    }

    @Transactional
    public UpdateFeedbackReplyResponse updateFeedbackReply(UpdateFeedbackReplyRequest request, Long feedbackNo, Long memberNo)
            throws BadRequestException, FirebaseMessagingException {

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

        // 피드백/문의사항 작성자에게 푸쉬 알림
        CreateAlarmRequest alarmRequest = CreateAlarmRequest.builder()
                .title(FEEDBACK_REPLY_FCM_TITLE)
                .targetMemberNo(member.getMemberNo())
                .alarmType(AlarmType.FEEDBACK)
                .targetPage(TargetPage.FEEDBACK)
                .targetId(updatedFeedback.getFeedbackNo())
                .fcmToken(member.getFcmToken())
                .build();
        alarmService.createPushAlarm(alarmRequest);

        return UpdateFeedbackReplyResponse.from(updatedFeedback);
    }

    @Transactional(readOnly = true)
    public FeedbackPageResponse<GetFeedbackResponse> getFeedbackScrollByAdminWithReply(Pageable pageable, Long memberNo, Boolean hasReply) {
        Member member = findMemberByNo(memberNo);

        feedbackChecker.isAdmin(member.getRole());

        Page<Feedback> feedbackPage = feedbackRepository.findByHasReply(hasReply, pageable);

        Page<GetFeedbackResponse> response = feedbackPage.map(GetFeedbackResponse::from);

        return FeedbackPageResponse.from(response);
    }

    private Member findMemberByNo(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);
    }

    private Feedback findFeedbackByNo(Long feedbackNo) {
        return feedbackRepository.findById(feedbackNo)
                .orElseThrow(EntityNotFoundException::new);
    }

}
