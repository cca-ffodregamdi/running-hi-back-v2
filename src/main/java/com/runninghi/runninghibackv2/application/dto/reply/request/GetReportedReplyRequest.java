package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;

public record GetReportedReplyRequest (
        Pageable pageable,
        long offset,
        String search,
        ProcessingStatus reportStatus
) {
    public static GetReportedReplyRequest of (Pageable pageable, String search, ProcessingStatus reportStatus) {
        return new GetReportedReplyRequest(
                pageable,
                (long) (pageable.getPageNumber() - 1)* pageable.getPageSize(),
                search,
                reportStatus
        );
    }
}
