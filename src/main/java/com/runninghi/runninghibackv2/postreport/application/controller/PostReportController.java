package com.runninghi.runninghibackv2.postreport.application.controller;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostReportController {

    private final PostReportService postReportService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("api/v1/postreports")
    public ResponseEntity<ApiResult> createPostReport(@RequestHeader(name = "Authorization") String bearerToken,
                                                      @RequestBody CreatePostReportRequest request) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);
        CreatePostReportResponse response = postReportService.createPostReport(memberNo, request);

        return ResponseEntity.ok(ApiResult.success("게시글신고 저장 성공", response));
    }

    @HasAccess
    @GetMapping("api/v1/postreports")
    public ResponseEntity<ApiResult> getPostReports() {

        List<GetPostReportResponse> response = postReportService.getPostReports();

        return ResponseEntity.ok(ApiResult.success("게시글신고 전체 조회 성공", response));
    }

    @HasAccess
    @GetMapping("api/v1/postreports/{postReportNo}")
    public ResponseEntity<ApiResult> getPostReportById(@PathVariable Long postReportNo) {

        GetPostReportResponse response = postReportService.getPostReportById(postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 상세 조회 성공", response));
    }

    @HasAccess
    @GetMapping("api/v1/postreports/posts")
    public ResponseEntity<ApiResult> getReportedPostsByStatus(@RequestParam String status) {

        List<Post> response = postReportService.getReportedPostsByStatus(status);

        return ResponseEntity.ok(ApiResult.success("신고된 게시글 전체 조회 성공", response));
    }

    @HasAccess
    @GetMapping("api/v1/postreports/posts/{postNo}")
    public ResponseEntity<ApiResult> getPostReportsByPostId(@PathVariable Long postNo) {

        List<GetPostReportResponse> response = postReportService.getPostReportsByPostId(postNo);

        return ResponseEntity.ok(ApiResult.success("특정 게시글의 신고 내역 조회 성공", response));
    }

    @HasAccess
    @PutMapping("api/v1/postreports/posts/{postNo}")
    public ResponseEntity<ApiResult> updatePostReport(@RequestBody UpdatePostReportRequest request,
                                                      @PathVariable Long postNo) {

        List<UpdatePostReportResponse> response = postReportService.updatePostReport(request, postNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 수정 성공", response));
    }

    @HasAccess
    @DeleteMapping("api/v1/postreports/{postReportNo}")
    public ResponseEntity<ApiResult> deletePostReport(@PathVariable Long postReportNo) {

        postReportService.deletePostReport(postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 삭제 성공", null));
    }
}
