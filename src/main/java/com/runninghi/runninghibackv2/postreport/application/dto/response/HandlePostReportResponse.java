package com.runninghi.runninghibackv2.postreport.application.dto.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;

public record HandlePostReportResponse(
        Long postReportNo,
        ReportCategory category,
        String content,
        ProcessingStatus status,
        boolean isPostDeleted
) {
    public static HandlePostReportResponse from(PostReport postReport) {

        return new HandlePostReportResponse(
                postReport.getPostReportNo(),
                postReport.getCategory(),
                postReport.getContent(),
                postReport.getStatus(),
                postReport.isPostDeleted());
    }
}
