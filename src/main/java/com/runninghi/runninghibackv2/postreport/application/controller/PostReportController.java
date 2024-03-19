package com.runninghi.runninghibackv2.postreport.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.response.CreatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.service.PostReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class PostReportController {

    private final PostReportService postReportService;

    @PostMapping("api/v1/postreports")
    public ResponseEntity<ApiResult> createPostReport(@RequestBody CreatePostReportRequest request) {

        try {
            // TODO. 신고자 정보 가져오기
            Member member = new Member.Builder()
                    .memberNo(1L)
                    .build();

            CreatePostReportResponse response = postReportService.createPostReport(request, member);

            return ResponseEntity.ok(ApiResult.success("게시글신고 저장 성공", response));

        } catch (Exception e) {
            // TODO. 예외에 따라 에러코드 세분화
            return ResponseEntity.ok(ApiResult.error(ErrorCode.INTER_SERVER_ERROR));
        }
    }
}
