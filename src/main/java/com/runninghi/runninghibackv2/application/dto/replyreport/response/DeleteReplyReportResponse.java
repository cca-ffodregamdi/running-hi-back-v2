package com.runninghi.runninghibackv2.application.dto.replyreport.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteReplyReportResponse(
        @Schema(description = "댓글 신고 Id", example = "1")
        Long replyReportNo
) {
    public static DeleteReplyReportResponse from(Long replyReportNo) {
        return new DeleteReplyReportResponse(replyReportNo);
    }
}
