package com.runninghi.runninghibackv2.application.dto.post.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateRecordResponse(
        @Schema(description = "게시글 번호", example = "1")
        Long postNo
) {

}