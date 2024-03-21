package com.runninghi.runninghibackv2.bookmark.application.dto.request;

public record CreateBookmarkRequest (
    Long memberNo,
    Long postNo
){
    public static CreateBookmarkRequest of (Long memberNo, Long postNo) {
        return new CreateBookmarkRequest(memberNo, postNo);
    }
}
