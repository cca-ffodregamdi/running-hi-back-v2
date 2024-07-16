package com.runninghi.runninghibackv2.application.dto.post.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RunDataRequest {
    @Schema(description = "러닝 시작 시각", example = "2024-03-29T13:16:06")
    LocalDateTime runStartDate;
    @Schema(description = "코스 위치", example = "서울특별시 성북구")
    String location;
    @Schema(description = "달린 거리(km)", example = "8.38")
    float distance;
    @Schema(description = "달린 시간", example = "42000(초)")
    int time;
    @Schema(description = "소모 칼로리 (kcal)", example = "280")
    int kcal;
    @Schema(description = "평균 페이스", example = "3000(초)")
    int meanPace;
    @Schema(description = "코스 난이도", example = "EASY")
    String difficulty;
    @Schema(description = "구간별 페이스", example = "[300, 250, 350, 400, 420]")
    List<Integer> sectionPace;
    @Schema(description = "구간별 소모 칼로리", example = "[10, 15, 20, 15, 30]")
    List<Integer> sectionKcal;
}
