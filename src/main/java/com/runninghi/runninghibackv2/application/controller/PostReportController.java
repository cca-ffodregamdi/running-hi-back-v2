package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.postreport.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.application.dto.postreport.response.*;
import com.runninghi.runninghibackv2.application.service.PostReportService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
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

@Tag(name = "게시글 신고 API", description = "게시글 신고 관련 API")
@RestController
@RequestMapping("/api/v1/post-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class PostReportController {

    private final PostReportService postReportService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "게시글 신고 저장", description = "게시글 신고를 저장합니다.")
    @PostMapping()
    public ResponseEntity<ApiResult<CreatePostReportResponse>> createPostReport(
            @RequestHeader(name = "Authorization") String bearerToken,
            @RequestBody CreatePostReportRequest request) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);
        CreatePostReportResponse response = postReportService.createPostReport(memberNo, request);

        return ResponseEntity.ok(ApiResult.success("게시글신고 저장 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글 신고 전체 조회", description = "저장된 모든 게시글 신고를 조회합니다.")
    @GetMapping()
    public ResponseEntity<ApiResult<Page<GetAllPostReportsResponse>>> getPostReports(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                                     @RequestParam(defaultValue = "10") @Positive int size,
                                                                                     @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));

        Page<GetAllPostReportsResponse> response = postReportService.getPostReports(pageable);

        return ResponseEntity.ok(ApiResult.success("게시글 신고 전체 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글 신고 상세 조회", description = "특정 게시글 신고의 상세 정보를 조회합니다.")
    @GetMapping("/{postReportNo}")
    public ResponseEntity<ApiResult<GetPostReportResponse>> getPostReportById(@PathVariable Long postReportNo) {

        GetPostReportResponse response = postReportService.getPostReportById(postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 상세 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글의 모든 신고 내역 조회", description = "특정 게시글의 모든 신고 내역을 조회합니다.")
    @GetMapping("/post")
    public ResponseEntity<ApiResult<List<GetAllPostReportsResponse>>> getPostReportsByPostId(
            @RequestParam Long postNo) {

        List<GetAllPostReportsResponse> response = postReportService.getPostReportScrollByPostId(postNo);

        return ResponseEntity.ok(ApiResult.success("게시글의 모든 신고 내역 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글의 모든 신고 내역 처리",
            description = "특정 게시글의 모든 신고를 수락/거절 처리합니다. 신고 수락 시 해당 게시글을 삭제합니다.")
    @PutMapping("/post")
    public ResponseEntity<ApiResult<List<HandlePostReportResponse>>> handlePostReports(
            @RequestParam Long postNo,
            @RequestParam boolean isAccepted) {

        List<HandlePostReportResponse> response = postReportService.handlePostReports(isAccepted, postNo);

        return ResponseEntity.ok(ApiResult.success("게시글의 모든 신고 내역 처리 성공", response));
    }

    @HasAccess
    @Operation(summary = "게시글 신고 삭제", description = "특정 게시글 신고를 삭제합니다.")
    @DeleteMapping("/{postReportNo}")
    public ResponseEntity<ApiResult<DeletePostReportResponse>> deletePostReport(@PathVariable Long postReportNo) {

        DeletePostReportResponse response = postReportService.deletePostReport(postReportNo);

        return ResponseEntity.ok(ApiResult.success("게시글신고 삭제 성공", response));
    }
}
