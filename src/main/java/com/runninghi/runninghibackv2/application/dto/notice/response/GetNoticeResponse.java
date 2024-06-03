package com.runninghi.runninghibackv2.application.dto.notice.response;

import com.runninghi.runninghibackv2.domain.entity.Notice;

import java.time.LocalDateTime;

public record GetNoticeResponse(
        Long noticeNo,
        String title,
        String content,
        LocalDateTime createDate
) {
    public static GetNoticeResponse from(Notice notice) {
        return new GetNoticeResponse(notice.getNoticeNo(), notice.getTitle(),
                notice.getContent(), notice.getCreateDate());
    }
}
