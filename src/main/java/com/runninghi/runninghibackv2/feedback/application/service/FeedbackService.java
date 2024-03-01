package com.runninghi.runninghibackv2.feedback.application.service;

import com.runninghi.runninghibackv2.feedback.application.dto.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.response.CreateFeedbackResponse;
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

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackDomainService feedbackDomainService;

    @Transactional
    public CreateFeedbackResponse createFeedback(CreateFeedbackRequest request, Long memberNo) {

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id"));

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
    public UpdateFeedbackResponse updateFeedback(UpdateFeedbackRequest request, Long memberNo) throws BadRequestException {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id"));

        Feedback feedback = feedbackRepository.findById(request.feedbackNo())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid feedback Id"));

        feedbackDomainService.isWriter(member.getMemberNo(), feedback.getFeedbackWriter().getMemberNo());
        feedbackDomainService.checkReplyStatus(feedback.isHasReply());
        feedbackDomainService.checkFeedbackValidation(request.title(), request.content());

        Feedback updatedFeedback = new Feedback.Builder()
                .feedbackNo(feedback.getFeedbackNo())
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
}
