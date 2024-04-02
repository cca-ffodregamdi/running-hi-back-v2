package com.runninghi.runninghibackv2.reply.application.dto.request;

import org.springframework.data.domain.Pageable;

public record GetReportedReplyRequest (
        Pageable pageable,
        String search
) {
    public static GetReportedReplyRequest of (Pageable pageable, String search) {
        return new GetReportedReplyRequest(pageable, search);
    }
}
