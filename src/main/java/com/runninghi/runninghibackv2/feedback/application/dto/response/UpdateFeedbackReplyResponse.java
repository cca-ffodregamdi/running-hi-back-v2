package com.runninghi.runninghibackv2.feedback.application.dto.response;

import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.Feedback;
import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.FeedbackCategory;

import java.time.LocalDateTime;

public record UpdateFeedbackReplyResponse(
        String title,
        String content,
        FeedbackCategory category,
        LocalDateTime createDate,
        LocalDateTime updateDate,
        boolean hasReply,
        String reply,
        String nickname
) {
    public static UpdateFeedbackReplyResponse from(Feedback feedback) {
        return new UpdateFeedbackReplyResponse(feedback.getTitle(), feedback.getContent(),
                feedback.getCategory(), feedback.getCreateDate(), feedback.getUpdateDate(),
                feedback.isHasReply(), feedback.getReply(), feedback.getFeedbackWriter().getNickname());
    }
}
