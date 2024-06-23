package com.runninghi.runninghibackv2.application.dto.postreport.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.entity.PostReport;
import io.swagger.v3.oas.annotations.media.Schema;

public record HandlePostReportResponse(
        @Schema(description = "게시글 신고 Id", example = "1")
        Long postReportNo,
        @Schema(description = "신고 사유 카테고리", example = "SPAM")
        ReportCategory category,
        @Schema(description = "기타 신고 사유", example = "공백 제외 10자 이상의 사유")
        String content,
        @Schema(description = "신고 처리 상태", example = "ACCEPTED")
        ProcessingStatus status
) {
    public static HandlePostReportResponse from(PostReport postReport) {

        return new HandlePostReportResponse(
                postReport.getPostReportNo(),
                postReport.getCategory(),
                postReport.getContent(),
                postReport.getStatus());
    }
}
