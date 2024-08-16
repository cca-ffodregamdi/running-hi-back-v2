package com.runninghi.runninghibackv2.application.dto.alarm.request;

import com.runninghi.runninghibackv2.domain.enumtype.AlarmType;
import com.runninghi.runninghibackv2.domain.enumtype.TargetPage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateAlarmRequest(
        @Schema(description = "알림 제목", example = "공지 알림")
        @NotBlank(message = "알림 제목을 입력해주세요.")
        String title,
        @Schema(description = "알림 생성 대상자 번호", example = "1")
        Long memberNo,
        @Schema(description = "알림 타입", example = "reply")
        AlarmType alarmType,
        @Schema(description = "이동해야 할 페이지", example = "Feed")
        TargetPage targetPage,
        @Schema(description = "관련 식별 값", example = "234")
        Long targetId
) {

        public static CreateAlarmRequest of (String title, Long memberNo, AlarmType alarmType, TargetPage targetPage,
                                             Long targetId) {
                return new CreateAlarmRequest(
                        title,
                        memberNo,
                        alarmType,
                        targetPage,
                        targetId
                );
        }
}
