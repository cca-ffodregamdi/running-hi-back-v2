package com.runninghi.runninghibackv2.replyreport.application.dto.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity.ReplyReport;

public record HandleReplyReportResponse(
        Long replyReportNo,
        ReportCategory category,
        String content,
        ProcessingStatus status,
        boolean isReplyDeleted
) {
    public static HandleReplyReportResponse from(ReplyReport replyReport) {

        return new HandleReplyReportResponse(
                replyReport.getReplyReportNo(),
                replyReport.getCategory(),
                replyReport.getContent(),
                replyReport.getStatus(),
                replyReport.isReplyDeleted());
    }
}
