package com.runninghi.runninghibackv2.application.dto.reply.response;

import com.runninghi.runninghibackv2.domain.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record GetPostReplyResponse (
        @Schema(description = "댓글 작성자 닉네임", example = "러너1")
        String memberName,
        @Schema(description = "댓글 내용", example = "댓글 내용")
        String replyContent,
        @Schema(description = "댓글 생성 일", example = "2024-03-27T13:23:12")
        LocalDateTime createDate
) {
    public static GetPostReplyResponse from (Reply reply) {
        return new GetPostReplyResponse(
                reply.getMember().getNickname(),
                reply.getReplyContent(),
                reply.getCreateDate()
        );
    }
}
