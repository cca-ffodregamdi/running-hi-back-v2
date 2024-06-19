package com.runninghi.runninghibackv2.application.dto.reply.response;

import com.runninghi.runninghibackv2.domain.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CreateReplyResponse (

        @Schema(description = "해당 게시글 좋아요 개수", example = "2")
        int likeCnt

)
{
}
