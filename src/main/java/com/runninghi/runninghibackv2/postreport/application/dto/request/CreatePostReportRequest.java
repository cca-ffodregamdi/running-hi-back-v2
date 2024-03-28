package com.runninghi.runninghibackv2.postreport.application.dto.request;

import com.runninghi.runninghibackv2.common.enumtype.ReportCategory;

public record CreatePostReportRequest(
        ReportCategory category,
        String content,
        Long reportedPostNo
)
{}
