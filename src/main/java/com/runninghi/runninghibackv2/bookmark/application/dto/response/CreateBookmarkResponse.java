package com.runninghi.runninghibackv2.bookmark.application.dto.response;

import com.runninghi.runninghibackv2.bookmark.domain.aggregate.entity.Bookmark;

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
