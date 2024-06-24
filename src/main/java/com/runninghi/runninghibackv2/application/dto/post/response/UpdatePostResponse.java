package com.runninghi.runninghibackv2.application.dto.post.response;

import com.runninghi.runninghibackv2.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdatePostResponse(
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "대표 기록", example = "28분 32초")
        String mainData,
        @Schema(description = "코스 난이도", example = "EASY")
        String difficulty

) {
    public static UpdatePostResponse from(Post post) {
        return new UpdatePostResponse(
                post.getPostContent(),
                post.getMainData(),
                post.getDifficulty().toString()
        );
    }
}
