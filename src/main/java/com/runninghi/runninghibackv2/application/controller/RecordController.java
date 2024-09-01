package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.record.response.GetMonthlyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetWeeklyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetYearlyRecordResponse;
import com.runninghi.runninghibackv2.application.service.RecordService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "사용자 일간/주간/월간 기록 API", description = "기록 페이지에서 나의 데이터를 일간, 주간, 월간으로 확인할 수 있는 API 입니다.")
@RestController
@RequestMapping("api/v1/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private  final JwtTokenProvider jwtTokenProvider;
    private static final String GET_MAPPING_RESPONSE_MESSAGE = "성공적으로 조회되었습니다.";

    @GetMapping("weekly")
    @Operation(summary = "주간 기록 & 게시글 확인", description = "기록 페이지에서 사용자의 러닝 기록을 주간 기준으로 확인합니다.")
    public ResponseEntity<ApiResult<GetWeeklyRecordResponse>> getWeeklyRecord(@RequestHeader("Authorization") String bearerToken,
                                                                              @RequestParam LocalDate date) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        GetWeeklyRecordResponse response = recordService.getWeeklyRecord(memberInfo.memberNo(), date);

        return ResponseEntity.ok(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @GetMapping("monthly")
    @Operation(summary = "월간 기록 & 게시글 확인", description = "기록 페이지에서 사용자의 러닝 기록을 월간 기준으로 확인합니다.")
    public ResponseEntity<ApiResult<GetMonthlyRecordResponse>> getMonthlyRecord(@RequestHeader("Authorization") String bearerToken,
                                                                          @RequestParam LocalDate date) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        GetMonthlyRecordResponse response = recordService.getMonthlyRecord(memberInfo.memberNo(), date);

        return ResponseEntity.ok(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @GetMapping("yearly")
    @Operation(summary = "연간 기록 & 게시글 확인", description = "기록 페이지에서 사용자의 러닝 기록을 연간 기준으로 확인합니다.")
    public ResponseEntity<ApiResult<GetYearlyRecordResponse>> getYearlyRecord(@RequestHeader("Authorization") String bearerToken,
                                                                              @RequestParam LocalDate date) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        GetYearlyRecordResponse response = recordService.getYearlyRecord(memberInfo.memberNo(), date);

        return ResponseEntity.ok(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, response));
    }
}
