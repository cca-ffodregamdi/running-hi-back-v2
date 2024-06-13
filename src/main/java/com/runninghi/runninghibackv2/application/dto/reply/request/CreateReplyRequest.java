package com.runninghi.runninghibackv2.application.dto.reply.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateReplyRequest(
        @Schema(description = "게시글 번호", example = "2")
        Long postNo,
        @Schema(description = "댓글 내용", example = "댓글 내용 테스트 ")
        String replyContent
) {}
