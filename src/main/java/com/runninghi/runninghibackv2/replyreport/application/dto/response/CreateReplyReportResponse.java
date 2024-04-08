package com.runninghi.runninghibackv2.replyreport.application.dto.response;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.common.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity.ReplyReport;

public record CreateReplyReportResponse(
        Long replyReportNo,
        ReportCategory category,
        String content,
        ProcessingStatus status,
        Member reporter,
        Reply reportedReply,
        String replyContent,
        boolean isReplyDeleted
) {
    public static CreateReplyReportResponse from(ReplyReport replyReport) {

        return new CreateReplyReportResponse(
                replyReport.getReplyReportNo(),
                replyReport.getCategory(),
                replyReport.getContent(),
                replyReport.getStatus(),
                replyReport.getReporter(),
                replyReport.getReportedReply(),
                replyReport.getReplyContent(),
                replyReport.isReplyDeleted());
    }
}
