package com.runninghi.runninghibackv2.application.dto.post.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreatePostResponse(
        @Schema(description = "게시글 번호", example = "1")
        Long postNo
) {

}