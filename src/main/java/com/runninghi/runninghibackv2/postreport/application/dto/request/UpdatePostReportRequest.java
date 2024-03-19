package com.runninghi.runninghibackv2.postreport.application.dto.request;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;

public record UpdatePostReportRequest(
        ProcessingStatus status,
        boolean reportedPostDeleted
) {}
