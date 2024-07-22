package com.runninghi.runninghibackv2.application.dto.alarm.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateAlarmRequest(
        @Schema(description = "알림 제목", example = "공지 알림")
        @NotBlank(message = "알림 제목을 입력해주세요.")
        String title,
        @Schema(description = "알림 내용", example = "알림 내용")
        @NotBlank(message = "알림 내용을 입력해주세요.")
        String content
) {
}
