package com.runninghi.runninghibackv2.application.dto.bookmark.response;

import com.runninghi.runninghibackv2.domain.entity.Bookmark;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "북마크 생성 응답")
public record CreateBookmarkResponse(

        @Schema(description = "회원 번호")
        Long memberNo,
        @Schema(description = "게시글 번호")
        Long postNo
) {

    public static CreateBookmarkResponse fromEntity (Bookmark bookmark) {
        return new CreateBookmarkResponse(
                bookmark.getBookmarkId().getMemberNo(),
                bookmark.getBookmarkId().getPostNo()
        );
    }
}
