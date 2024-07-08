package com.runninghi.runninghibackv2.application.dto.record.response;

import com.runninghi.runninghibackv2.application.dto.post.response.GetRecordPostResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetYearlyRecordResponse(
        @Schema(description = "1년 동안 뛴 거리 리스트", example = "[2.8, 3.0, 4.2, 1.4, 0, 0, 0]")
        List<Float> yearlyRecordData,
        @Schema(description = "1년 동안 달린 전체 시간 (초 단위)", example = "21600")
        int totalTime,
        @Schema(description = "1년 평균 페이스 (초 단위)", example = "300")
        int meanPace,
        @Schema(description = "1년 동안 소모한 전체 칼로리", example = "800")
        int totalKcal,
        @Schema(description = "해당 날짜 구간에 해당하는 기록", example = "")
        List<GetRecordPostResponse> recordPostList
) {
}
