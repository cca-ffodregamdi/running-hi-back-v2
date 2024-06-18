package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;

public record GetReportedReplyRequest (
        Pageable pageable,
        String search,
        ProcessingStatus reportStatus
) {
    public static GetReportedReplyRequest of (Pageable pageable, String search, String reportStatus) {
        return new GetReportedReplyRequest(
                pageable,
                search,
                reportStatus.equals("ALL") ? null : ProcessingStatus.valueOf(reportStatus)
        );
    }

}
