package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.replyreport.response.DeleteReplyReportResponse;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.application.dto.replyreport.request.CreateReplyReportRequest;
import com.runninghi.runninghibackv2.application.dto.replyreport.response.CreateReplyReportResponse;
import com.runninghi.runninghibackv2.application.dto.replyreport.response.GetReplyReportResponse;
import com.runninghi.runninghibackv2.application.dto.replyreport.response.HandleReplyReportResponse;
import com.runninghi.runninghibackv2.application.service.ReplyReportService;
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

@Tag(name = "댓글 신고 API", description = "댓글 신고 관련 API")
@RestController
@RequestMapping("/api/v1/reply-reports")
@RequiredArgsConstructor
public class ReplyReportController {

    private final ReplyReportService replyReportService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "댓글 신고 저장", description = "댓글 신고를 저장합니다.")
    @PostMapping()
    public ResponseEntity<ApiResult> createReplyReport(@RequestHeader(name = "Authorization") String bearerToken,
                                                       @RequestBody CreateReplyReportRequest request) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);
        CreateReplyReportResponse response = replyReportService.createReplyReport(memberNo, request);

        return ResponseEntity.ok(ApiResult.success("댓글신고 저장 성공", response));
    }

    @HasAccess
    @Operation(summary = "댓글 신고 전체 조회", description = "저장된 모든 댓글 신고를 조회합니다.")
    @GetMapping()
    public ResponseEntity<ApiResult> getReplyReports() {

        List<GetReplyReportResponse> response = replyReportService.getReplyReports();

        return ResponseEntity.ok(ApiResult.success("댓글신고 전체 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "댓글 신고 상세 조회", description = "특정 댓글 신고의 상세 정보를 조회합니다.")
    @GetMapping("/{replyReportNo}")
    public ResponseEntity<ApiResult> getReplyReportById(@PathVariable Long replyReportNo) {

        GetReplyReportResponse response = replyReportService.getReplyReportById(replyReportNo);

        return ResponseEntity.ok(ApiResult.success("댓글신고 상세 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "댓글의 모든 신고 내역 조회", description = "특정 댓글의 모든 신고 내역을 페이지 형태로 조회합니다.")
    @GetMapping("/reply")
    public ResponseEntity<ApiResult> getReplyReportsByReplyId(@RequestParam Long replyNo,
                                                              @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                              @RequestParam(defaultValue = "10") @Positive int size,
                                                              @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));
        Page<GetReplyReportResponse> response = replyReportService.getReplyReportScrollByReplyId(replyNo, pageable);

        return ResponseEntity.ok(ApiResult.success("댓글의 모든 신고 내역 조회 성공", response));
    }

    @HasAccess
    @Operation(summary = "댓글의 모든 신고 내역 처리",
            description = "특정 댓글의 모든 신고를 수락/거절 처리합니다. 신고 수락 시 해당 댓글을 삭제합니다.")
    @PutMapping("/reply")
    public ResponseEntity<ApiResult> handleReplyReports(@RequestParam Long replyNo,
                                                        @RequestParam boolean isAccepted) {

        List<HandleReplyReportResponse> response = replyReportService.handleReplyReports(isAccepted, replyNo);

        return ResponseEntity.ok(ApiResult.success("댓글의 모든 신고 내역 처리 성공", response));
    }

    @HasAccess
    @Operation(summary = "댓글 신고 삭제", description = "특정 댓글 신고를 삭제합니다.")
    @DeleteMapping("/{replyReportNo}")
    public ResponseEntity<ApiResult<DeleteReplyReportResponse>> deleteReplyReport(@PathVariable Long replyReportNo) {

        DeleteReplyReportResponse response = replyReportService.deleteReplyReport(replyReportNo);

        return ResponseEntity.ok(ApiResult.success("댓글신고 삭제 성공", response));
    }
}
