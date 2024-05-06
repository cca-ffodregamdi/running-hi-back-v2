package com.runninghi.runninghibackv2.application.dto.feedback.response;

import com.runninghi.runninghibackv2.domain.entity.Feedback;
import com.runninghi.runninghibackv2.domain.enumtype.FeedbackCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "피드백 답변 응답")
public record UpdateFeedbackReplyResponse(
        @Schema(description = "피드백 번호", example = "1")
        Long feedbackNo,
        @Schema(description = "제목", example = "서비스 개선 요청 / 문의사항")
        String title,
        @Schema(description = "내용", example = "서비스 이용 중 발견한 문제 / 문의사항에 대한 상세 설명")
        String content,
        @Schema(description = "카테고리", example = "INQUIRY")
        FeedbackCategory category,
        @Schema(description = "작성일시", example = "2024-03-27T13:23:12")
        LocalDateTime createDate,
        @Schema(description = "수정일시", example = "2024-03-27T13:23:12")
        LocalDateTime updateDate,
        @Schema(description = "답변 여부", example = "true")
        boolean hasReply,
        @Schema(description = "피드백 / 문의사항에 대한 답변", example = "고객님의 문의사항에 대해 답변드립니다.")
        String reply,
        @Schema(description = "작성자 닉네임", example = "러너 96541953")
        String nickname
) {
    public static UpdateFeedbackReplyResponse from(Feedback feedback) {
        return new UpdateFeedbackReplyResponse(feedback.getFeedbackNo(), feedback.getTitle(), feedback.getContent(),
                feedback.getCategory(), feedback.getCreateDate(), feedback.getUpdateDate(),
                feedback.isHasReply(), feedback.getReply(), feedback.getFeedbackWriter().getNickname());
    }
}
