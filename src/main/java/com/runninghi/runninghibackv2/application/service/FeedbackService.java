package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.feedback.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.response.*;
import com.runninghi.runninghibackv2.domain.entity.Feedback;
import com.runninghi.runninghibackv2.domain.entity.Member;
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
    public UpdateFeedbackReplyResponse updateFeedbackReply(UpdateFeedbackReplyRequest request, Long feedbackNo, Long memberNo) throws BadRequestException {

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

        return UpdateFeedbackReplyResponse.from(updatedFeedback);
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
