package com.runninghi.runninghibackv2.application.dto.alarm.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record GetAllAlarmResponse(
        @Schema(description = "알림 제목", example = "공지 알림")
        String title,
        @Schema(description = "알림 내용", example = "버전 업데이트 v1.2.1")
        String content,
        @Schema(description = "알림 확인 여부", example = "false")
        boolean isRead,
        @Schema(description = "알림 생성 시간", example = "2024-06-01T00:00:00")
        LocalDateTime createDate
) {
}
