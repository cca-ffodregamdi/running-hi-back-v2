package com.runninghi.runninghibackv2.postreport.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.request.UpdatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.response.CreatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.dto.response.GetPostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.dto.response.UpdatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.service.PostReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostReportController {

    private final PostReportService postReportService;

    @PostMapping("api/v1/postreports")
    public ResponseEntity<ApiResult> createPostReport(@RequestBody CreatePostReportRequest request) {

        // TODO. 신고자 정보 가져오기
        Long memberNo = 1L;
        CreatePostReportResponse response = postReportService.createPostReport(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 저장 성공", response));
    }

    @GetMapping("api/v1/postreports/{postReportNo}")
    public ResponseEntity<ApiResult> getPostReport(@PathVariable Long postReportNo) {

        GetPostReportResponse response = postReportService.getPostReportById(postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 상세조회 성공", response));
    }

    @PutMapping("api/v1/postreports/{postReportNo}")
    public ResponseEntity<ApiResult> updatePostReport(@RequestBody UpdatePostReportRequest request,
                                                      @PathVariable Long postReportNo) {

        UpdatePostReportResponse response = postReportService.updatePostReport(request, postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 수정 성공", response));
    }

    @DeleteMapping("api/v1/postreports/{postReportNo}")
    public ResponseEntity<ApiResult> deletePostReport(@PathVariable Long postReportNo) {

        postReportService.deletePostReport(postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 삭제 성공", null));
    }
}
