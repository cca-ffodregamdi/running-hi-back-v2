package com.runninghi.runninghibackv2.application.dto.bookmark.response;

import com.runninghi.runninghibackv2.domain.entity.Bookmark;

public record CreateBookmarkResponse(
        Long memberNo,
        Long postNo
) {

    public static CreateBookmarkResponse fromEntity (Bookmark bookmark) {
        return new CreateBookmarkResponse(
                bookmark.getBookmarkId().getMemberNo(),
                bookmark.getBookmarkId().getPostNo()
        );
    }
}
