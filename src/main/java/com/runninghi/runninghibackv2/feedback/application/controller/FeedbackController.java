package com.runninghi.runninghibackv2.feedback.application.controller;

import com.runninghi.runninghibackv2.feedback.application.service.FeedbackReplyService;
import com.runninghi.runninghibackv2.feedback.application.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackReplyService feedbackReplyService;

    // 피드백 작성

    // 피드백 조회

    // 전체 피드백 조회 : 관리자

    // 피드백 삭제


}
