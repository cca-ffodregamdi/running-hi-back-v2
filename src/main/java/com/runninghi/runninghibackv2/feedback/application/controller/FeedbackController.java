package com.runninghi.runninghibackv2.feedback.application.controller;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.feedback.application.dto.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.response.*;
import com.runninghi.runninghibackv2.feedback.application.service.FeedbackService;
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
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final JwtTokenProvider jwtTokenProvider;

    // 피드백 작성
    @PostMapping("/api/v1/feedbacks")
    public ResponseEntity<ApiResult> createFeedback(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateFeedbackRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        CreateFeedbackResponse response = feedbackService.createFeedback(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 저장 성공", response));
    }

    // 피드백 상세 조회
    @GetMapping("/api/v1/feedbacks/{feedbackNo}")
    public ResponseEntity<ApiResult> getFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        GetFeedbackResponse response = feedbackService.getFeedback(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 조회 성공", response));
    }

    // 전체 피드백 리스트 조회
    @GetMapping("/api/v1/feedbacks")
    public ResponseEntity<ApiResult> getFeedbackScroll(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc", message = "Sort must be 'asc' or 'desc'") String sort
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        Sort.Direction direction = sort.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createDate"));

        Page<GetFeedbackResponse> response = feedbackService.getFeedbackScroll(pageable, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 페이지 조회 성공", response));
    }

    // 피드백 상세 조회 : 관리자
    @HasAccess
    @GetMapping("/api/v1/feedbacks/admin/{feedbackNo}")
    public ResponseEntity<ApiResult> getFeedbackByAdmin(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        GetFeedbackResponse response = feedbackService.getFeedbackByAdmin(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 조회 성공 : 관리자", response));
    }


    // 전체 피드백 리스트 조회 : 관리자
    @HasAccess
    @GetMapping("/api/v1/feedbacks/admin")
    public ResponseEntity<ApiResult> getFeedbackScrollByAdmin(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc", message = "Sort must be 'asc' or 'desc'") String sort
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        Sort.Direction direction = sort.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createDate"));

        Page<GetFeedbackResponse> response = feedbackService.getFeedbackScrollByAdmin(pageable, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 리스트 조회 성공 : 관리자", response));
    }

    // 피드백 삭제
    @DeleteMapping("/api/v1/feedbacks/{feedbackNo}")
    public ResponseEntity<ApiResult> deleteFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        DeleteFeedbackResponse response = feedbackService.deleteFeedback(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 삭제 성공", response));
    }

    // 피드백 수정
    @PutMapping("/api/v1/feedbacks/{feedbackNo}")
    public ResponseEntity<ApiResult> updateFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo,
            @RequestBody UpdateFeedbackRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        UpdateFeedbackResponse response = feedbackService.updateFeedback(request, feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 수정 성공", response));
    }

    // 피드백 답변 작성 및 수정 : 관리자
    @HasAccess
    @PutMapping("/api/v1/feedbacks/admin/{feedbackNo}")
    public ResponseEntity<ApiResult> updateFeedbackReply(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo,
            @RequestBody UpdateFeedbackReplyRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        UpdateFeedbackReplyResponse response = feedbackService.updateFeedbackReply(request, feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 답변 작성/수정 완료 : 관리자", response));

    }
}
