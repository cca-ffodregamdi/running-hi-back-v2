package com.runninghi.runninghibackv2.application.dto.like.response;

import com.runninghi.runninghibackv2.domain.entity.Like;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좋아요 생성 응답")
public record CreateLikeResponse(
        @Schema(description = "요청한 회원 번호")
        Long memberNo,
        @Schema(description = "좋아요 눌린 게시글 번호")
        Long postNo
) {

    public static CreateLikeResponse fromEntity (Like like) {
        return new CreateLikeResponse(
                like.getLikeId().getMemberNo(),
                like.getLikeId().getPostNo()
        );
    }
}
