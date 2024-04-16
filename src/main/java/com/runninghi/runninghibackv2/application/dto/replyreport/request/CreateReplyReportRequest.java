package com.runninghi.runninghibackv2.application.dto.replyreport.request;

import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateReplyReportRequest(
        @Schema(description = "신고 유형", example = "SPAM")
        ReportCategory category,
        @Schema(description = "기타 신고 사유", example = "공백 제외 10자 이상의 사유")
        String content,
        @Schema(description = "신고된 댓글 번호", example = "1")
        Long reportedReplyNo
)
{}
