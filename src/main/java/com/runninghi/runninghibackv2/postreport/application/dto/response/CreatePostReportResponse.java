package com.runninghi.runninghibackv2.postreport.application.dto.response;

import com.runninghi.runninghibackv2.common.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;

public record CreatePostReportResponse(
        Long postReportNo,
        ReportCategory category,
        String content,
        boolean reportedPostDeleted,
        Member reporter,
        Member reportedMember,
        Post reportedPost
) {
    public static CreatePostReportResponse from(PostReport postReport) {

        return new CreatePostReportResponse(
                postReport.getPostReportNo(),
                postReport.getCategory(),
                postReport.getContent(),
                postReport.isReportedPostDeleted(),
                postReport.getReporter(),
                postReport.getReportedMember(),
                postReport.getReportedPost());
    }
}
