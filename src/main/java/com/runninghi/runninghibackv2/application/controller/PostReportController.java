package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.postreport.response.DeletePostReportResponse;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.application.dto.postreport.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.application.dto.postreport.response.CreatePostReportResponse;
import com.runninghi.runninghibackv2.application.dto.postreport.response.GetPostReportResponse;
import com.runninghi.runninghibackv2.application.dto.postreport.response.HandlePostReportResponse;
import com.runninghi.runninghibackv2.application.service.PostReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시글 신고 컨트롤러", description = "게시글 신고 API")
@RestController
@RequestMapping("/api/v1/post-reports")
@RequiredArgsConstructor
public class PostReportController {

    private final PostReportService postReportService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "게시글 신고 저장")
    @PostMapping()
    public ResponseEntity<ApiResult<CreatePostReportResponse>> createPostReport(
            @RequestHeader(name = "Authorization") String bearerToken,
            @RequestBody CreatePostReportRequest request) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);
        CreatePostReportResponse response = postReportService.createPostReport(memberNo, request);

        return ResponseEntity.ok(ApiResult.success("게시글신고 저장 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글 신고 전체 조회")
    @GetMapping()
    public ResponseEntity<ApiResult<List<GetPostReportResponse>>> getPostReports() {

        List<GetPostReportResponse> response = postReportService.getPostReports();

        return ResponseEntity.ok(ApiResult.success("게시글신고 전체 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글 신고 상세 조회")
    @GetMapping("/{postReportNo}")
    public ResponseEntity<ApiResult<GetPostReportResponse>> getPostReportById(@PathVariable Long postReportNo) {

        GetPostReportResponse response = postReportService.getPostReportById(postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 상세 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "신고 처리상태로 신고된 게시글 리스트 조회")
    @GetMapping("/post/status")
    public ResponseEntity<ApiResult<List<Post>>> getReportedPostsByStatus(@RequestParam ProcessingStatus status) {

        List<Post> response = postReportService.getReportedPostsByStatus(status);

        return ResponseEntity.ok(ApiResult.success("신고된 게시글 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글의 모든 신고 내역 조회")
    @GetMapping("/post")
    public ResponseEntity<ApiResult<Page<GetPostReportResponse>>> getPostReportsByPostId(
            @RequestParam Long postNo,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));
        Page<GetPostReportResponse> response = postReportService.getPostReportScrollByPostId(postNo, pageable);

        return ResponseEntity.ok(ApiResult.success("게시글의 모든 신고 내역 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글의 모든 신고 내역 처리")
    @PutMapping("/post")
    public ResponseEntity<ApiResult<List<HandlePostReportResponse>>> handlePostReports(
            @RequestParam Long postNo,
            @RequestParam boolean isAccepted) {

        List<HandlePostReportResponse> response = postReportService.handlePostReports(isAccepted, postNo);

        return ResponseEntity.ok(ApiResult.success("게시글의 모든 신고 내역 처리 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글 신고 삭제")
    @DeleteMapping("/{postReportNo}")
    public ResponseEntity<ApiResult<DeletePostReportResponse>> deletePostReport(@PathVariable Long postReportNo) {

        DeletePostReportResponse response = postReportService.deletePostReport(postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 삭제 성공", response));
    }
}
