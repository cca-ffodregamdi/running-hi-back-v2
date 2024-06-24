package com.runninghi.runninghibackv2.application.dto.post.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UpdatePostRequest(
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "대표 기록", example = "1")
        int mainData,
        @Schema(description = "코스 난이도", example = "EASY")
        String difficulty
) {
}
