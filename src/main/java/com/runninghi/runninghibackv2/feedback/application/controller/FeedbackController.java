package com.runninghi.runninghibackv2.feedback.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.feedback.application.dto.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.response.CreateFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.dto.response.UpdateFeedbackResponse;
import com.runninghi.runninghibackv2.feedback.application.service.FeedbackReplyService;
import com.runninghi.runninghibackv2.feedback.application.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackReplyService feedbackReplyService;

    // 피드백 작성
    @PostMapping
    public ResponseEntity<ApiResult> createFeedback(@RequestBody CreateFeedbackRequest request){

        Long memberNo = 1L;

        CreateFeedbackResponse response = feedbackService.createFeedback(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 저장 성공", response));
    }

    // 피드백 조회

    // 전체 피드백 조회 : 관리자

    // 피드백 삭제

    // 피드백 수정
    @PutMapping
    public ResponseEntity<ApiResult> updateFeedback(@RequestBody UpdateFeedbackRequest request) throws BadRequestException {

        Long memberNo = 1L;

        UpdateFeedbackResponse response = feedbackService.updateFeedback(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 수정 성공", response));
    }

}
