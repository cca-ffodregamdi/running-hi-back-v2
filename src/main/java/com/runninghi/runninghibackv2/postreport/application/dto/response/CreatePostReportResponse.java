package com.runninghi.runninghibackv2.postreport.application.dto.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;

public record CreatePostReportResponse(
        Long postReportNo,
        ReportCategory category,
        String content,
        ProcessingStatus status,
        Member reporter,
        Post reportedPost,
        boolean isPostDeleted
) {
    public static CreatePostReportResponse from(PostReport postReport) {

        return new CreatePostReportResponse(
                postReport.getPostReportNo(),
                postReport.getCategory(),
                postReport.getContent(),
                postReport.getStatus(),
                postReport.getReporter(),
                postReport.getReportedPost(),
                postReport.isPostDeleted());
    }
}
