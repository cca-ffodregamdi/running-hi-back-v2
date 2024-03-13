package com.runninghi.runninghibackv2.post.application.dto.response;

public record CreatePostResponse(
        float distance,
        float time,
        float kcal,
        float speed,
        float meanPace
) {

}