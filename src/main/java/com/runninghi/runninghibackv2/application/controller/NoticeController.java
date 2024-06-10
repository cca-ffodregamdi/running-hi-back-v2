package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.notice.request.CreateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.request.UpdateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.response.*;
import com.runninghi.runninghibackv2.application.service.NoticeService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "공지사항 API", description = "공지사항 관련 API")
public class NoticeController {

    private final NoticeService noticeService;
    private final JwtTokenProvider jwtTokenProvider;

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
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
            },
            responses = @ApiResponse(responseCode = "200", description = "공지사항 생성 성공")
    )
    public ResponseEntity<ApiResult<CreateNoticeResponse>> createNotice(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateNoticeRequest request
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
        CreateNoticeResponse response = noticeService.createNotice(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("공지사항 생성 성공", response));
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
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "noticeNo", description = "수정하고자 하는 공지사항의 고유 번호", required = true)
            },
            responses = @ApiResponse(responseCode = "200", description = "공지사항 수정 성공")
    )
    public ResponseEntity<ApiResult<UpdateNoticeResponse>> updateNotice(
            @Parameter(description = "공지사항 ID") @PathVariable Long noticeNo,
            @RequestBody UpdateNoticeRequest request
    ) {

        UpdateNoticeResponse response = noticeService.updateNotice(noticeNo, request);

        return ResponseEntity.ok(ApiResult.success("공지사항 수정 성공", response));
    }

    /**
     * 공지사항 조회 메소드입니다.
     * @param noticeNo 공지사항 ID
     * @return 조회된 공지사항 정보
     */
    @GetMapping(value = "/api/v1/notices/{noticeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "공지사항 조회",
            description = "특정 공지사항을 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "noticeNo", description = "조회하고자 하는 공지사항의 고유 번호", required = true)
            },
            responses = @ApiResponse(responseCode = "200", description = "공지사항 조회 성공")
    )
    public ResponseEntity<ApiResult<GetNoticeResponse>> getNotice(
            @Parameter(description = "공지사항 ID") @PathVariable Long noticeNo
    ) {

        GetNoticeResponse response = noticeService.getNotice(noticeNo);

        return ResponseEntity.ok(ApiResult.success("공지사항 조회 성공", response));
    }

    /**
     * 모든 공지사항 리스트 조회 메소드입니다.
     * @return 조회된 모든 공지사항 리스트
     */
    @GetMapping(value = "/api/v1/notices", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "모든 공지사항 조회",
            description = "모든 공지사항 리스트를 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
            },
            responses = @ApiResponse(responseCode = "200", description = "모든 공지사항 조회 성공")
    )
    public ResponseEntity<ApiResult<NoticePageResponse<GetNoticeResponse>>> getAllNotices(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));
        NoticePageResponse<GetNoticeResponse> response = noticeService.getAllNotices(pageable);

        return ResponseEntity.ok(ApiResult.success("모든 공지사항 조회 성공", response));
    }

    /**
     * 공지사항 삭제 메소드입니다.
     * @param noticeNo 공지사항 ID
     * @return 삭제 성공 여부
     */
    @HasAccess
    @DeleteMapping(value = "/api/v1/notices/{noticeNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "공지사항 삭제",
            description = "기존 공지사항을 삭제합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "noticeNo", description = "삭제하고자 하는 공지사항의 고유 번호", required = true)
            },
            responses = @ApiResponse(responseCode = "200", description = "공지사항 삭제 성공")
    )
    public ResponseEntity<ApiResult<DeleteNoticeResponse>> deleteNotice(
            @Parameter(description = "공지사항 ID") @PathVariable Long noticeNo
    ) {
        DeleteNoticeResponse response = noticeService.deleteNotice(noticeNo);
        return ResponseEntity.ok(ApiResult.success("공지사항 삭제 성공", response));
    }

}
