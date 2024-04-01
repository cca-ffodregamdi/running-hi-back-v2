package com.runninghi.runninghibackv2.post.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreatePostResponse(
        @Schema(description = "게시글 번호", example = "1")
        Long postNo,
        @Schema(description = "코스 거리", example = "3.04")
        float distance,
        @Schema(description = "코스 시간", example = "30.5")
        float time,
        @Schema(description = "소모 칼로리", example = "50.02")
        float kcal,
        @Schema(description = "평균 속도", example = "5.9")
        float speed,
        @Schema(description = "평균 페이스 (분/km)", example = "10.03")
        float meanPace
) {

}