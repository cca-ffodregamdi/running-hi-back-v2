package com.runninghi.runninghibackv2.application.dto.replyreport.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.entity.ReplyReport;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetReplyReportResponse(
        @Schema(description = "댓글 신고 Id", example = "1")
        Long replyReportNo,
        @Schema(description = "신고 사유 카테고리", example = "SPAM")
        ReportCategory category,
        @Schema(description = "기타 신고 사유", example = "공백 제외 10자 이상의 사유")
        String content,
        @Schema(description = "신고 처리 상태", example = "ACCEPTED")
        ProcessingStatus status,
        @Schema(description = "신고자 번호", example = "1")
        Long reporterNo,
        @Schema(description = "신고된 댓글 번호", example = "1")
        Long reportedReplyNo,
        @Schema(description = "신고된 댓글 내용", example = "1")
        String replyContent
) {
    public static GetReplyReportResponse from(ReplyReport replyReport) {
        Long replyNo = replyReport.getReportedReply() != null ? replyReport.getReportedReply().getReplyNo() : null;

        return new GetReplyReportResponse(
                replyReport.getReplyReportNo(),
                replyReport.getCategory(),
                replyReport.getContent(),
                replyReport.getStatus(),
                replyReport.getReporter().getMemberNo(),
                replyNo,
                replyReport.getReplyContent());
    }
}
