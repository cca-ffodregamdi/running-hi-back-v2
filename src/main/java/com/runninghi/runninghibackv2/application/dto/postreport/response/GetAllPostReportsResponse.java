package com.runninghi.runninghibackv2.application.dto.postreport.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.entity.PostReport;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetAllPostReportsResponse(
        @Schema(description = "게시글 신고 Id", example = "1")
        Long postReportNo,
        @Schema(description = "신고 사유 카테고리", example = "SPAM")
        ReportCategory category,
        @Schema(description = "기타 신고 사유", example = "공백 제외 10자 이상의 사유")
        String content,
        @Schema(description = "신고 처리 상태", example = "ACCEPTED")
        ProcessingStatus status,
        @Schema(description = "신고자 번호", example = "1")
        Long reporterNo,
        @Schema(description = "신고된 게시글 번호", example = "1")
        Long reportedPostNo,
        @Schema(description = "신고된 게시글 내용", example = "1")
        String postContent
) {
    public static GetAllPostReportsResponse from(PostReport postReport) {

        return new GetAllPostReportsResponse(
                postReport.getPostReportNo(),
                postReport.getCategory(),
                postReport.getContent(),
                postReport.getStatus(),
                postReport.getReporter().getMemberNo(),
                postReport.getReportedPost().getPostNo(),
                postReport.getPostContent()
        );
    }
}
