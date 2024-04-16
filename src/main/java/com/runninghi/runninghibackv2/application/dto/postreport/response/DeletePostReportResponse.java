package com.runninghi.runninghibackv2.application.dto.postreport.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeletePostReportResponse(
        @Schema(description = "게시글 신고 Id", example = "1")
        Long postReportNo
) {
    public static DeletePostReportResponse from(Long postReportNo) {
        return new DeletePostReportResponse(postReportNo);
    }
}
