package com.runninghi.runninghibackv2.reply.application.dto.request;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import org.springframework.data.domain.Pageable;

public record GetReportedReplyRequest (
        Pageable pageable,
        String search,

        ProcessingStatus reportStatus
) {
    public static GetReportedReplyRequest of (Pageable pageable, String search, ProcessingStatus reportStatus) {
        return new GetReportedReplyRequest(pageable, search, reportStatus);
    }
}
