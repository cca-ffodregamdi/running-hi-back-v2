package com.runninghi.runninghibackv2.feedback.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.feedback.application.dto.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.DeleteFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.GetFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.response.CreateFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.dto.response.DeleteFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.dto.response.GetFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.dto.response.UpdateFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 피드백 작성
    @PostMapping("/api/v1/feedback")
    public ResponseEntity<ApiResult> createFeedback(@RequestBody CreateFeedbackRequest request){

        Long memberNo = 1L;

        CreateFeedbackResponse response = feedbackService.createFeedback(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 저장 성공", response));
    }

    // 피드백 상세 조회
    @GetMapping("/api/v1/feedback/{feedbackNo}")
    public ResponseEntity<ApiResult> getFeedback(@PathVariable("feedbackNo") Long feedbackNo) throws BadRequestException {

        Long memberNo = 1L;

        GetFeedbackResponse response = feedbackService.getFeedback(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 조회 성공", response));
    }

    // 전체 피드백 리스트 조회

    // 피드백 상세 조회 : 관리자
    @GetMapping("/api/v1/feedback/admin/{feedbackNo}")
    public ResponseEntity<ApiResult> getFeedbackByAdmin(@PathVariable("feedbackNo") Long feedbackNo) throws AuthenticationException {

        Long memberNo = 1L;

        GetFeedbackResponse response = feedbackService.getFeedbackByAdmin(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 조회 성공 : 관리자", response));
    }


    // 전체 피드백 리스트 조회 : 관리자

    // 피드백 삭제
    @DeleteMapping("/api/v1/feedback/{feedbackNo}")
    public ResponseEntity<ApiResult> deleteFeedback(@PathVariable("feedbackNo") Long feedbackNo) throws BadRequestException {

        Long memberNo = 1L;

        DeleteFeedbackResponse response = feedbackService.deleteFeedback(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 삭제 성공", response));
    }

    // 피드백 수정
    @PutMapping("/api/v1/feedback/{feedbackNo}")
    public ResponseEntity<ApiResult> updateFeedback(@PathVariable("feedbackNo") Long feedbackNo, UpdateFeedbackRequest request) throws BadRequestException {

        Long memberNo = 1L;

        UpdateFeedbackResponse response = feedbackService.updateFeedback(request, feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 수정 성공", response));
    }

}
