package com.runninghi.runninghibackv2.application.dto.post.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "피드백 삭제 응답")
public record DeletePostResponse(
        @Schema(description = "삭제된 Post Id", example = "1")
        Long postNo
) {
    public static DeletePostResponse from(Long postNo) {
        return new DeletePostResponse(postNo);
    }
}