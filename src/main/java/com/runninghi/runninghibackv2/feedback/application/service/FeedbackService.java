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

    private static final String INVALID_MEMBER_ID_MESSAGE = "Invalid member Id";
    private static final String INVALID_FEEDBACK_ID_MESSAGE = "Invalid feedback Id";


    @Transactional
    public CreateFeedbackResponse createFeedback(CreateFeedbackRequest request, Long memberNo) {

        Member member = findMemberByNo(memberNo);

        feedbackChecker.checkFeedbackValidation(request.title(), request.content());

        Feedback feedback = new Feedback.Builder()
                .feedbackWriter(member)
                .title(request.title())
                .content(request.content())
                .category(FeedbackCategory.getFeedbackCategoryFromValue(request.category()))
                .hasReply(false)
                .builder();

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

        Feedback updatedFeedback = new Feedback.Builder()
                .feedbackNo(feedbackNo)
                .feedbackWriter(feedback.getFeedbackWriter())
                .title(request.title())
                .content(request.content())
                .category(FeedbackCategory.getFeedbackCategoryFromValue(request.category()))
                .hasReply(false)
                .builder();

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
    public Page<GetFeedbackResponse> getFeedbackScroll(Pageable pageable, Long memberNo) {

        Member member = findMemberByNo(memberNo);

        Page<Feedback> feedbackPage = feedbackRepository.findAllByFeedbackWriter(member, pageable);

        return feedbackPage.map(GetFeedbackResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<GetFeedbackResponse> getFeedbackScrollByAdmin(Pageable pageable, Long memberNo) {
        Member member = findMemberByNo(memberNo);

        feedbackChecker.isAdmin(member.getRole());

        Page<Feedback> feedbackPage = feedbackRepository.findAllBy(pageable);

        return feedbackPage.map(GetFeedbackResponse::from);
    }

    @Transactional
    public UpdateFeedbackReplyResponse updateFeedbackReply(UpdateFeedbackReplyRequest request, Long feedbackNo, Long memberNo) {

        Member member = findMemberByNo(memberNo);
        feedbackChecker.isAdmin(member.getRole());

        Feedback feedback = findFeedbackByNo(feedbackNo);

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

        return UpdateFeedbackReplyResponse.from(updatedFeedback);
    }

    private Member findMemberByNo(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_MEMBER_ID_MESSAGE));
    }

    private Feedback findFeedbackByNo(Long feedbackNo) {
        return feedbackRepository.findById(feedbackNo)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_FEEDBACK_ID_MESSAGE));
    }
}
