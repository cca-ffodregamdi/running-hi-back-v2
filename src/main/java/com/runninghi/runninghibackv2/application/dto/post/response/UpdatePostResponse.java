package com.runninghi.runninghibackv2.application.dto.post.response;

import com.runninghi.runninghibackv2.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdatePostResponse(
        @Schema(description = "게시글 제목", example = "제목 예시")
        String postTitle,
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent

) {
    public static UpdatePostResponse from(Post post) {
        return new UpdatePostResponse(
                post.getPostTitle(),
                post.getPostContent()
        );
    }
}
