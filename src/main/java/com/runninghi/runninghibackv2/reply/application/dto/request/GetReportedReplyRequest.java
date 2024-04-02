package com.runninghi.runninghibackv2.reply.application.dto.request;

import org.springframework.data.domain.Pageable;

public record GetReportedReplyRequest(
        Pageable pageable,
        String search,
        int reportStatus
) {
    public static GetReportedReplyRequest of (Pageable pageable, String search, int reportStatus) {
        return new GetReportedReplyRequest(pageable, search, reportStatus);
    }
}
