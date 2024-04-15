package com.runninghi.runninghibackv2.application.dto.replyreport.response;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Reply;
import com.runninghi.runninghibackv2.domain.entity.ReplyReport;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateReplyReportResponse(
        @Schema(description = "댓글 신고 Id", example = "1")
        Long replyReportNo,
        @Schema(description = "신고 카테고리", example = "SPAM")
        ReportCategory category,
        @Schema(description = "기타 신고 사유", example = "공백 제외 10자 이상의 사유")
        String content
) {
    public static CreateReplyReportResponse from(ReplyReport replyReport) {

        return new CreateReplyReportResponse(
                replyReport.getReplyReportNo(),
                replyReport.getCategory(),
                replyReport.getContent());
    }
}
