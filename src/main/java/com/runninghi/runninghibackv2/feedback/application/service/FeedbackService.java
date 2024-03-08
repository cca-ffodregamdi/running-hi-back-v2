package com.runninghi.runninghibackv2.feedback.application.service;

import com.runninghi.runninghibackv2.feedback.application.dto.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.response.*;
import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.Feedback;
import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.FeedbackCategory;
import com.runninghi.runninghibackv2.feedback.domain.repository.FeedbackRepository;
import com.runninghi.runninghibackv2.feedback.domain.service.FeedbackChecker;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackChecker feedbackChecker;

    private static final String INVALID_MEMBER_ID_MESSAGE = "Invalid member Id";
    private static final String INVALID_FEEDBACK_ID_MESSAGE = "Invalid feedback Id";


    @Transactional
    public CreateFeedbackResponse createFeedback(CreateFeedbackRequest request, Long memberNo) {

        Member member = getMember(memberNo);

        feedbackChecker.checkFeedbackValidation(request.title(), request.content());

        Feedback feedback = new Feedback.Builder()
                .feedbackWriter(member)
                .title(request.title())
                .content(request.content())
                .category(FeedbackCategory.getFeedbackCategoryFromValue(request.category()))
                .hasReply(false)
                .builder();

        feedbackRepository.save(feedback);

        return CreateFeedbackResponse.create(feedback);
    }

    @Transactional
    public UpdateFeedbackResponse updateFeedback(UpdateFeedbackRequest request, Long feedbackNo, Long memberNo) throws BadRequestException {

        Member member = getMember(memberNo);
        Feedback feedback = getFeedback(feedbackNo);

        feedbackChecker.isWriter(member.getMemberNo(), feedback.getFeedbackWriter().getMemberNo());
        feedbackChecker.checkReplyStatus(feedback.isHasReply());
        feedbackChecker.checkFeedbackValidation(request.title(), request.content());

        Feedback updatedFeedback = new Feedback.Builder()
                .feedbackNo(feedbackNo)
                .feedbackWriter(feedback.getFeedbackWriter())
                .title(request.title())
                .content(request.content())
                .category(FeedbackCategory.getFeedbackCategoryFromValue(request.category()))
                .hasReply(false)
                .builder();

        feedbackRepository.save(updatedFeedback);

        return UpdateFeedbackResponse.create(updatedFeedback);
    }

    @Transactional
    public DeleteFeedbackResponse deleteFeedback(Long feedbackNo, Long memberNo) throws BadRequestException {

        Member member = getMember(memberNo);
        Feedback feedback = getFeedback(feedbackNo);

        feedbackChecker.isWriter(member.getMemberNo(), feedback.getFeedbackWriter().getMemberNo());

        feedbackRepository.delete(feedback);

        return DeleteFeedbackResponse.create(feedbackNo);

    }

    @Transactional(readOnly = true)
    public GetFeedbackResponse getFeedback(Long feedbackNo, Long memberNo) throws BadRequestException {
        Feedback feedback = getFeedback(feedbackNo);

        feedbackChecker.isWriter(memberNo, feedback.getFeedbackWriter().getMemberNo());

        return GetFeedbackResponse.create(feedback);
    }

    @Transactional(readOnly = true)
    public GetFeedbackResponse getFeedbackByAdmin(Long feedbackNo, Long memberNo) throws AuthenticationException {

        Member member = getMember(memberNo);
        Feedback feedback = getFeedback(feedbackNo);

        feedbackChecker.isAdmin(member.getRole());

        return GetFeedbackResponse.create(feedback);

    }

    @Transactional(readOnly = true)
    public Page<GetFeedbackResponse> getFeedbackScroll(Pageable pageable, Long memberNo) {

        Member member = getMember(memberNo);

        Page<Feedback> feedbackPage = feedbackRepository.findAllByFeedbackWriter(member, pageable);

        return feedbackPage.map(GetFeedbackResponse::create);
    }

    @Transactional(readOnly = true)
    public Page<GetFeedbackResponse> getFeedbackScrollByAdmin(Pageable pageable, Long memberNo) throws AuthenticationException {
        Member member = getMember(memberNo);

        feedbackChecker.isAdmin(member.getRole());

        Page<Feedback> feedbackPage = feedbackRepository.findAllBy(pageable);

        return feedbackPage.map(GetFeedbackResponse::create);
    }

    @Transactional
    public UpdateFeedbackReplyResponse updateFeedbackReply(UpdateFeedbackReplyRequest request, Long feedbackNo, Long memberNo) throws AuthenticationException {

        Member member = getMember(memberNo);
        Feedback feedback = getFeedback(feedbackNo);

        feedbackChecker.isAdmin(member.getRole());

        Feedback updatedFeedback = new Feedback.Builder()
                .feedbackNo(feedbackNo)
                .feedbackWriter(feedback.getFeedbackWriter())
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .category(feedback.getCategory())
                .hasReply(true)
                .reply(request.content())
                .builder();

        feedbackRepository.save(updatedFeedback);

        return UpdateFeedbackReplyResponse.create(updatedFeedback);
    }

    private Member getMember(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_MEMBER_ID_MESSAGE));
    }

    private Feedback getFeedback(Long feedbackNo) {
        return feedbackRepository.findById(feedbackNo)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_FEEDBACK_ID_MESSAGE));
    }
}
