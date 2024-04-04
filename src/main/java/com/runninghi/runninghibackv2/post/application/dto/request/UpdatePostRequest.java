package com.runninghi.runninghibackv2.post.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UpdatePostRequest(
        @Schema(description = "게시글 제목", example = "제목 예시")
        String postTitle,
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "키워드 목록", example = "보통,강아지랑,경사없음")
        List<String> keywordList
) {
}
