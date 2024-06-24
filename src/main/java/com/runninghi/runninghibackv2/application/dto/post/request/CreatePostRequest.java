package com.runninghi.runninghibackv2.application.dto.post.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreatePostRequest(
        @Schema(description = "게시글 번호", example = "1")
        Long postNo,
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "코스 난이도", example = "EASY")
        String difficulty,
        @Schema(description = "대표 기록", example = "0")
        int mainData,
        @Schema(description = "이미지 URL", example = "https://picsum.photos/200")
        String imageUrl
) {

}
