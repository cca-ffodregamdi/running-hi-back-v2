package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.notice.request.CreateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.request.UpdateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.response.CreateNoticeResponse;
import com.runninghi.runninghibackv2.application.dto.notice.response.DeleteNoticeResponse;
import com.runninghi.runninghibackv2.application.dto.notice.response.GetNoticeResponse;
import com.runninghi.runninghibackv2.application.dto.notice.response.UpdateNoticeResponse;
import com.runninghi.runninghibackv2.application.service.NoticeService;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 생성 메소드입니다.
     * @param request 공지사항 생성 요청 데이터
     * @return 생성된 공지사항 정보
     */
    @HasAccess
    @PostMapping(value = "/api/v1/notices", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "공지사항 생성",
            description = "새로운 공지사항을 생성합니다.",
            responses = @ApiResponse(responseCode = "200", description = "공지사항 생성 성공")
    )
    public ResponseEntity<ApiResult<CreateNoticeResponse>> createNotice(@RequestBody CreateNoticeRequest request) {

        CreateNoticeResponse response = noticeService.createNotice(request);
        return ResponseEntity.ok(ApiResult.success("", response));

    }

    /**
     * 공지사항 수정 메소드입니다.
     * @param noticeNo 공지사항 ID
     * @param request 공지사항 수정 요청 데이터
     * @return 수정된 공지사항 정보
     */
    @HasAccess
    @PutMapping(value = "/api/v1/notices/{noticeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "공지사항 수정",
            description = "기존 공지사항을 수정합니다.",
            responses = @ApiResponse(responseCode = "200", description = "공지사항 수정 성공")
    )
    public ResponseEntity<ApiResult<UpdateNoticeResponse>> updateNotice(
            @Parameter(description = "공지사항 ID") @PathVariable Long noticeNo,
            @RequestBody UpdateNoticeRequest request) {

        UpdateNoticeResponse response = noticeService.updateNotice(noticeNo, request);
        return ResponseEntity.ok(ApiResult.success("", response));
    }

    /**
     * 공지사항 조회 메소드입니다.
     * @param noticeNo 공지사항 ID
     * @return 조회된 공지사항 정보
     */
    @HasAccess
    @GetMapping(value = "/api/v1/notices/{noticeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "공지사항 조회",
            description = "특정 공지사항을 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "공지사항 조회 성공")
    )
    public ResponseEntity<ApiResult<GetNoticeResponse>> getNotice(@Parameter(description = "공지사항 ID") @PathVariable Long noticeNo) {
        GetNoticeResponse response = noticeService.getNotice(noticeNo);
        return ResponseEntity.ok(ApiResult.success("", response));
    }

    /**
     * 모든 공지사항 리스트 조회 메소드입니다.
     * @return 조회된 모든 공지사항 리스트
     */
    @HasAccess
    @GetMapping(value = "/api/v1/notices", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "모든 공지사항 조회",
            description = "모든 공지사항 리스트를 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "모든 공지사항 조회 성공")
    )
    public ResponseEntity<ApiResult<List<GetNoticeResponse>>> getAllNotices() {
        List<GetNoticeResponse> response = noticeService.getAllNotices();
        return ResponseEntity.ok(ApiResult.success("", response));
    }

    /**
     * 공지사항 삭제 메소드입니다.
     * @param noticeNo 공지사항 ID
     * @return 삭제 성공 여부
     */
    @HasAccess
    @DeleteMapping(value = "/api/v1/notice/{noticeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "공지사항 삭제",
            description = "기존 공지사항을 삭제합니다.",
            responses = @ApiResponse(responseCode = "200", description = "공지사항 삭제 성공")
    )public ResponseEntity<ApiResult<DeleteNoticeResponse>> deleteNotice(@Parameter(description = "공지사항 ID") @PathVariable Long noticeNo) {
        DeleteNoticeResponse response = noticeService.deleteNotice(noticeNo);
        return ResponseEntity.ok(ApiResult.success("", response));
    }

}
