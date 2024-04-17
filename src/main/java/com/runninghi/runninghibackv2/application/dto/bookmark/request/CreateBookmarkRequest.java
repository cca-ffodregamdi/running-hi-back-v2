package com.runninghi.runninghibackv2.application.dto.bookmark.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "북마크 생성 요청")
public record CreateBookmarkRequest(

        @Schema(description = "요청 회원 번호", example = "1")
        Long memberNo,
        @Schema(description = "요청 게시물 번호", example = "1")
        Long postNo
){
    public static CreateBookmarkRequest of (Long memberNo, Long postNo) {
        return new CreateBookmarkRequest(memberNo, postNo);
    }
}
