package com.runninghi.runninghibackv2.application.dto.record.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordData {
    @Schema(description = "달린 거리(km)", example = "8.4")
    float distance;
    @Schema(description = "달린 시간(초)", example = "18000")
    int time;
    @Schema(description = "평균 페이스(초)", example = "480")
    int meanPace;
    @Schema(description = "소모 칼로리", example = "200")
    int kcal;
}
