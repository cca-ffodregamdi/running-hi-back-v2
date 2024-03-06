package com.runninghi.runninghibackv2.feedback.application.service;

import com.runninghi.runninghibackv2.feedback.application.dto.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.response.CreateFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.dto.response.DeleteFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.dto.response.GetFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.dto.response.UpdateFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.Feedback;
import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.FeedbackCategory;
import com.runninghi.runninghibackv2.feedback.domain.repository.FeedbackRepository;
import com.runninghi.runninghibackv2.feedback.domain.service.FeedbackDomainService;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackDomainService feedbackDomainService;

    private static final String INVALID_MEMBER_ID_MESSAGE = "Invalid member Id";
    private static final String INVALID_FEEDBACK_ID_MESSAGE = "Invalid feedback Id";


    @Transactional
    public CreateFeedbackResponse createFeedback(CreateFeedbackRequest request, Long memberNo) {

        Member member = getMember(memberNo);

        feedbackDomainService.checkFeedbackValidation(request.title(), request.content());

        Feedback feedback = new Feedback.Builder()
                .feedbackWriter(member)
                .title(request.title())
                .content(request.content())
                .category(FeedbackCategory.getFeedbackCategoryFromValue(request.category()))
                .hasReply(false)
                .builder();

        feedbackRepository.save(feedback);

        return CreateFeedbackResponse.create(feedback.getFeedbackNo(), feedback.getTitle(), feedback.getContent());
    }

    @Transactional
    public UpdateFeedbackResponse updateFeedback(UpdateFeedbackRequest request, Long feedbackNo, Long memberNo) throws BadRequestException {

        Member member = getMember(memberNo);
        Feedback feedback = getFeedback(feedbackNo);

        feedbackDomainService.isWriter(member.getMemberNo(), feedback.getFeedbackWriter().getMemberNo());
        feedbackDomainService.checkReplyStatus(feedback.isHasReply());
        feedbackDomainService.checkFeedbackValidation(request.title(), request.content());

        Feedback updatedFeedback = new Feedback.Builder()
                .feedbackNo(feedbackNo)
                .feedbackWriter(feedback.getFeedbackWriter())
                .title(request.title())
                .content(request.content())
                .category(FeedbackCategory.getFeedbackCategoryFromValue(request.category()))
                .hasReply(false)
                .builder();

        feedbackRepository.save(updatedFeedback);

        return UpdateFeedbackResponse.create(updatedFeedback.getFeedbackNo(), updatedFeedback.getTitle(),
                updatedFeedback.getContent(), updatedFeedback.getCategory().getDescription());
    }

    @Transactional
    public DeleteFeedbackResponse deleteFeedback(Long feedbackNo, Long memberNo) throws BadRequestException {

        Member member = getMember(memberNo);
        Feedback feedback = getFeedback(feedbackNo);

        feedbackDomainService.isWriter(member.getMemberNo(), feedback.getFeedbackWriter().getMemberNo());

        feedbackRepository.delete(feedback);

        return DeleteFeedbackResponse.create(feedbackNo);

    }

    @Transactional(readOnly = true)
    public GetFeedbackResponse getFeedback(Long feedbackNo, Long memberNo) throws BadRequestException {
        Feedback feedback = getFeedback(feedbackNo);

        feedbackDomainService.isWriter(memberNo, feedback.getFeedbackWriter().getMemberNo());

        return GetFeedbackResponse.create(feedback.getTitle(), feedback.getContent(), feedback.getCategory(),
                feedback.getCreateDate(), feedback.getUpdateDate(), feedback.isHasReply(), feedback.getReply(),
                feedback.getFeedbackWriter().getNickname());
    }

    @Transactional(readOnly = true)
    public GetFeedbackResponse getFeedbackByAdmin(Long feedbackNo, Long memberNo) throws AuthenticationException {

        Member member = getMember(memberNo);
        Feedback feedback = getFeedback(feedbackNo);

        feedbackDomainService.isAdmin(member.getRole());

        return GetFeedbackResponse.create(feedback.getTitle(), feedback.getContent(), feedback.getCategory(),
                feedback.getCreateDate(), feedback.getUpdateDate(), feedback.isHasReply(), feedback.getReply(),
                feedback.getFeedbackWriter().getNickname());

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
