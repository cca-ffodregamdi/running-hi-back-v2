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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
@Tag(name = "알림 API", description = "알림 관련 API")
public class AlarmController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AlarmService alarmService;


    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader(value = "Authorization") String accessToken) {
        Long memberNo = jwtTokenProvider.getMemberNoFromToken(accessToken);

        return alarmService.sseSubscribe(memberNo);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<List<GetAllAlarmResponse>>> getAllPushAlarms(@Parameter(description = "사용자 인증을 위한 Bearer Token")
                                                                                @RequestHeader(value = "Authorization") String token) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
        List<GetAllAlarmResponse> allAlarmResponses = alarmService.getAllPushAlarms(memberNo);

        return ResponseEntity.ok().body(ApiResult.success("알림 리스트 조회 성공", allAlarmResponses));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<Void>> createPushAlarm(@Valid @RequestBody CreateAlarmRequest request) throws FirebaseMessagingException {

        alarmService.createPushAlarm(request);

        return ResponseEntity.ok().body(ApiResult.success("알림 생성 성공", null));
    }

    @DeleteMapping(value = "/{alarmNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<Void>> deleteAlarm(@PathVariable("alarmNo") Long alarmNo) throws FirebaseMessagingException {

        alarmService.deleteAlarm(alarmNo);

        return ResponseEntity.ok().body(ApiResult.success("알림 삭제 성공", null));
    }

}
