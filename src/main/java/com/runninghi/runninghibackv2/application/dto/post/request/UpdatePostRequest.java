package com.runninghi.runninghibackv2.application.dto.post.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UpdatePostRequest(
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "키워드 목록", example = "[\"강아지랑\", \"초보자용\"]")
        List<String> keywordList,
        @Schema(description = "코스 난이도", example = "EASY")
        String difficulty
) {
}
