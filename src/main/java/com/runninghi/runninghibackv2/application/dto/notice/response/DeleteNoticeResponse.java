package com.runninghi.runninghibackv2.application.dto.notice.response;

public record DeleteNoticeResponse(
        Long noticeNo
) {
    public static DeleteNoticeResponse from(Long noticeNo) {
        return new DeleteNoticeResponse(noticeNo);
    }
}
