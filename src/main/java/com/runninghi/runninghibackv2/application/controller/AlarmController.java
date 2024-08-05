package com.runninghi.runninghibackv2.application.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.runninghi.runninghibackv2.application.dto.alarm.request.CreateAlarmRequest;
import com.runninghi.runninghibackv2.application.dto.alarm.response.GetAllAlarmResponse;
import com.runninghi.runninghibackv2.application.service.AlarmService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
@Tag(name = "알림 API", description = "알림 관련 API")
public class AlarmController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AlarmService alarmService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<List<GetAllAlarmResponse>>> getAllAlarm(@Parameter(description = "사용자 인증을 위한 Bearer Token")
                                                                                @RequestHeader(value = "Authorization") String token) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
        List<GetAllAlarmResponse> allAlarmResponses = alarmService.getAllAlarm(memberNo);

        return ResponseEntity.ok().body(ApiResult.success("알림 리스트 조회 성공", allAlarmResponses));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<Void>> createAlarm(@Valid @RequestBody CreateAlarmRequest request) throws FirebaseMessagingException {

        alarmService.createAlarm(request);

        return ResponseEntity.ok().body(ApiResult.success("알림 생성 성공", null));
    }

    @PutMapping(value = "/{alarmNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<Void>> readAlarm(@PathVariable("alarmNo") Long alarmNo) throws FirebaseMessagingException {

        alarmService.readAlarm(alarmNo);

        return ResponseEntity.ok().body(ApiResult.success("알림 읽음 처리 성공", null));
    }

}
