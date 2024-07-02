package com.runninghi.runninghibackv2.application.dto.like.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좋아요 생성 응답")
public record LikeResponse(
        @Schema(description = "좋아요 개수")
        Integer likeCnt
) {

    public static LikeResponse of(Integer likeCnt) {
        return new LikeResponse(likeCnt);
    }
}
