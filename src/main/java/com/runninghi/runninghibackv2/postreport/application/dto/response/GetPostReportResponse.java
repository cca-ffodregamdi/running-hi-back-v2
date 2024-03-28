package com.runninghi.runninghibackv2.postreport.application.dto.response;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.common.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;

public record GetPostReportResponse(
        Long postReportNo,
        ReportCategory category,
        String content,
        ProcessingStatus status,
        boolean reportedPostDeleted,
        Post reportedPost
) {
    public static GetPostReportResponse from(PostReport postReport) {

        return new GetPostReportResponse(
                postReport.getPostReportNo(),
                postReport.getCategory(),
                postReport.getContent(),
                postReport.getStatus(),
                postReport.isPostDeleted(),
                postReport.getReportedPost());
    }
}
