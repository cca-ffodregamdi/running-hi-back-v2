package com.runninghi.runninghibackv2.application.dto.record.response;

import com.runninghi.runninghibackv2.application.dto.post.response.GetRecordPostResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public record GetWeeklyRecordResponse(
        @Schema(description = "1주일 동안 뛴 데이터 리스트  [{거리, 시간,페이스,칼로리}")
        List<RecordData> weeklyRecordData,
        @Schema(description = "전체 러닝 횟수", example = "30")
        int runCnt,
        @Schema(description = "1주 동안 달린 전체 시간 (초 단위)", example = "21600")
        int totalTime,
        @Schema(description = "1주 평균 페이스 (초 단위)", example = "300")
        int meanPace,
        @Schema(description = "1주 동안 소모한 전체 칼로리", example = "800")
        int totalKcal,
        @Schema(description = "해당 날짜 구간에 해당하는 기록")
        List<GetRecordPostResponse> recordPostList

) {
}
