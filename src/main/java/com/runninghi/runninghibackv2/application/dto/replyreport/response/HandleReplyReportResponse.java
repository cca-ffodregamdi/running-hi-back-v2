package com.runninghi.runninghibackv2.application.dto.replyreport.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.entity.ReplyReport;
import io.swagger.v3.oas.annotations.media.Schema;

public record HandleReplyReportResponse(
        @Schema(description = "댓글 신고 Id", example = "1")
        Long replyReportNo,
        @Schema(description = "신고 사유 카테고리", example = "SPAM")
        ReportCategory category,
        @Schema(description = "기타 신고 사유", example = "공백 제외 10자 이상의 사유")
        String content,
        @Schema(description = "신고 처리 상태", example = "ACCEPTED")
        ProcessingStatus status,
        @Schema(description = "연관된 댓글 삭제 여부", example = "false")
        boolean isReplyDeleted
) {
    public static HandleReplyReportResponse from(ReplyReport replyReport) {

        return new HandleReplyReportResponse(
                replyReport.getReplyReportNo(),
                replyReport.getCategory(),
                replyReport.getContent(),
                replyReport.getStatus(),
                replyReport.isReplyDeleted());
    }
}
