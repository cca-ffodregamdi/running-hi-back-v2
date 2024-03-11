package com.runninghi.runninghibackv2.feedback.application.dto.response;

import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.Feedback;
import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.FeedbackCategory;

import java.time.LocalDateTime;

public record GetFeedbackResponse(
        String title,
        String content,
        FeedbackCategory category,
        LocalDateTime createDate,
        LocalDateTime updateDate,
        boolean hasReply,
        String reply,
        String nickname
) {
    public static GetFeedbackResponse from(Feedback feedback) {
        return new GetFeedbackResponse(feedback.getTitle(), feedback.getContent(),
                feedback.getCategory(), feedback.getCreateDate(), feedback.getUpdateDate(),
                feedback.isHasReply(), feedback.getReply(), feedback.getFeedbackWriter().getNickname());
    }
}
