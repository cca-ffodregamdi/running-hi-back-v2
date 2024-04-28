package com.runninghi.runninghibackv2.application.dto.image.response;

import com.runninghi.runninghibackv2.domain.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateImageResponse (

        @Schema(description = "이미지 저장 번호", example = "1")
        Long memberNo,
        @Schema(description = "이미지 url", example = "sample.com")
        String imageUrl,
        @Schema(description = "관련 게시글 번호", example = "1")
        Long postNo
) {

    public static CreateImageResponse fromEntity(Image image) {
        return new CreateImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getPostNo()
        );
    }
}
