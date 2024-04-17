package com.runninghi.runninghibackv2.application.dto.reply.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateReplyRequest(
        @Schema(description = "댓글 작성자 번호", example = "2")
        Long memberNo,
        @Schema(description = "게시글 번호", example = "1")
        Long postNo,
        @Schema(description = "댓글 내용", example = "댓글 내용 테스트 ")
        String replyContent,
        @Schema(description = "부모 댓글 번호", example = "1")
        Long parentReplyNo
) {}
