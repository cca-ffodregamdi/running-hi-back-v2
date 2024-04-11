package com.runninghi.runninghibackv2.replyreport.application.dto.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity.ReplyReport;

public record GetReplyReportResponse(
        Long replyReportNo,
        ReportCategory category,
        String content,
        ProcessingStatus status,
        Long reporterNo,
        Long reportedReplyNo,
        String replyContent,
        boolean isReplyDeleted
) {
    public static GetReplyReportResponse from(ReplyReport replyReport) {

        return new GetReplyReportResponse(
                replyReport.getReplyReportNo(),
                replyReport.getCategory(),
                replyReport.getContent(),
                replyReport.getStatus(),
                replyReport.getReporter().getMemberNo(),
                replyReport.getReportedReply().getReplyNo(),
                replyReport.getReplyContent(),
                replyReport.isReplyDeleted());
    }
}
