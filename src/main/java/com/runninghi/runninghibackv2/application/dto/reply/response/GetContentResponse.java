package com.runninghi.runninghibackv2.application.dto.reply.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetContentResponse<T>(
        @Schema(description = "댓글 리스트")
        T content
) {
}
