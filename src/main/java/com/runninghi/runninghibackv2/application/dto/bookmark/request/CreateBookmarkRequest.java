package com.runninghi.runninghibackv2.application.dto.bookmark.request;

public record CreateBookmarkRequest (
    Long memberNo,
    Long postNo
){
    public static CreateBookmarkRequest of (Long memberNo, Long postNo) {
        return new CreateBookmarkRequest(memberNo, postNo);
    }
}
