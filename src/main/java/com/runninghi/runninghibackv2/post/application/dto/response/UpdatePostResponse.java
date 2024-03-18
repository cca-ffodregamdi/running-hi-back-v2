package com.runninghi.runninghibackv2.post.application.dto.response;

import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;

public record UpdatePostResponse(
        String postTitle,
        String postContent

) {
    public static UpdatePostResponse from(Post post) {
        return new UpdatePostResponse(
                post.getPostTitle(),
                post.getPostContent()
        );
    }
}
