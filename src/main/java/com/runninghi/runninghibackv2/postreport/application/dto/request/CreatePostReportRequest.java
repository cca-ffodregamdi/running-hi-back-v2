package com.runninghi.runninghibackv2.postreport.application.dto.request;

import com.runninghi.runninghibackv2.common.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;

public record CreatePostReportRequest(
        Long postReportNo,
        ReportCategory category,
        String content,
        boolean reportedPostDeleted,
        Member reporter,
        Member reportedMember,
        Post reportedPost
)
{}
