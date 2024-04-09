package com.runninghi.runninghibackv2.feedback.application.controller;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.feedback.application.dto.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.response.*;
import com.runninghi.runninghibackv2.feedback.application.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "피드백 API", description = "피드백/문의사항 관련 API")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/v1/feedbacks")
    @Operation(summary = "피드백 작성", description = "피드백/문의사항을 작성합니다.")
    public ResponseEntity<ApiResult<CreateFeedbackResponse>> createFeedback(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateFeedbackRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        CreateFeedbackResponse response = feedbackService.createFeedback(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 저장 성공", response));
    }

    @GetMapping("/api/v1/feedbacks/{feedbackNo}")
    @Operation(summary = "피드백 상세 조회", description = "특정 피드백/문의사항을 조회합니다.")
    public ResponseEntity<ApiResult<GetFeedbackResponse>> getFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        GetFeedbackResponse response = feedbackService.getFeedback(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 조회 성공", response));
    }

    @GetMapping("/api/v1/feedbacks")
    @Operation(summary = "전체 피드백 리스트 조회", description = "전체 피드백/문의사항 리스트를 조회합니다.")
    public ResponseEntity<ApiResult<Page<GetFeedbackResponse>>> getFeedbackScroll(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));

        Page<GetFeedbackResponse> response = feedbackService.getFeedbackScroll(pageable, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 페이지 조회 성공", response));
    }

    @HasAccess
    @GetMapping("/api/v1/feedbacks/admin/{feedbackNo}")
    @Operation(summary = "피드백 상세 조회 (관리자)", description = "특정 피드백/문의사항을 관리자가 조회합니다.")
    public ResponseEntity<ApiResult<GetFeedbackResponse>> getFeedbackByAdmin(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        GetFeedbackResponse response = feedbackService.getFeedbackByAdmin(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("", response));
    }

    @HasAccess
    @GetMapping("/api/v1/feedbacks/admin")
    @Operation(summary = "전체 피드백 리스트 조회 (관리자)", description = "관리자가 전체 피드백/문의사항 리스트를 조회합니다.")
    public ResponseEntity<ApiResult<Page<GetFeedbackResponse>>> getFeedbackScrollByAdmin(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));

        Page<GetFeedbackResponse> response = feedbackService.getFeedbackScrollByAdmin(pageable, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 리스트 조회 성공 : 관리자", response));
    }

    @DeleteMapping("/api/v1/feedbacks/{feedbackNo}")
    @Operation(summary = "피드백 삭제", description = "특정 피드백/문의사항을 삭제합니다.")
    public ResponseEntity<ApiResult<DeleteFeedbackResponse>> deleteFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        DeleteFeedbackResponse response = feedbackService.deleteFeedback(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 삭제 성공", response));
    }

    @PutMapping("/api/v1/feedbacks/{feedbackNo}")
    @Operation(summary = "피드백 수정", description = "특정 피드백/문의사항을 수정합니다.")
    public ResponseEntity<ApiResult<UpdateFeedbackResponse>> updateFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo,
            @RequestBody UpdateFeedbackRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        UpdateFeedbackResponse response = feedbackService.updateFeedback(request, feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 수정 성공", response));
    }

    @HasAccess
    @PutMapping("/api/v1/feedbacks/admin/{feedbackNo}")
    @Operation(summary = "피드백 답변 작성/수정 (관리자)", description = "관리자가 피드백 답변을 작성 또는 수정합니다.")
    public ResponseEntity<ApiResult<UpdateFeedbackReplyResponse>> updateFeedbackReply(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo,
            @RequestBody UpdateFeedbackReplyRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        UpdateFeedbackReplyResponse response = feedbackService.updateFeedbackReply(request, feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 답변 작성/수정 완료 : 관리자", response));

    }
}
