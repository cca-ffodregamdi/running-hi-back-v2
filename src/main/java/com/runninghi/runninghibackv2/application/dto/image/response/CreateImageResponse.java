package com.runninghi.runninghibackv2.application.dto.image.response;

import com.runninghi.runninghibackv2.domain.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateImageResponse (

        @Schema(description = "이미지 저장 번호", example = "1")
        Long memberNo,
        @Schema(description = "이미지 url", example = "sample.com")
        String imageUrl,
        @Schema(description = "관련 엔티티", example = "POST")
        ImageTarget imageTarget,
        @Schema(description = "관련 엔티티 식별 값", example = "1")
        Long targetNo
) {

    public static CreateImageResponse fromEntity(Image image) {
        return new CreateImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getImageTarget(),
                image.getTargetNo()
        );
    }
}
