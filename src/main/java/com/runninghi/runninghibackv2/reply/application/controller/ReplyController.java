package com.runninghi.runninghibackv2.reply.application.controller;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.reply.application.dto.request.CreateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.request.DeleteReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.request.GetReportedReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.request.UpdateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.response.CreateReplyResponse;
import com.runninghi.runninghibackv2.reply.application.dto.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.reply.application.dto.response.UpdateReplyResponse;
import com.runninghi.runninghibackv2.reply.application.service.ReplyService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reply")
public class ReplyController {

    private final ReplyService replyService;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String GET_MAPPING_RESPONSE_MESSAGE = "성공적으로 조회되었습니다.";
    private static final String CREATE_RESPONSE_MESSAGE = "성공적으로 생성되었습니다.";
    private static final String UPDATE_RESPONSE_MESSAGE = "성공적으로 수정되었습니다.";
    private static final String DELETE_RESPONSE_MESSAGE = "성공적으로 삭제되었습니다.";
    private static final int SEARCH_MAX_LENGTH = 20;

    @GetMapping("/{postNo}")
    public ResponseEntity<ApiResult> getReplyList(@PathVariable(name = "postNo") Long postNo) {

        List<GetReplyListResponse> replyList =  replyService.getReplyList(postNo);

        return ResponseEntity.ok().body(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, replyList));
    }

    @GetMapping("/byMember")
    public ResponseEntity<ApiResult> getReplyListByMemberNo(@RequestHeader(name = "memberNo") Long memberNo) {

        List<GetReplyListResponse> replyList = replyService.getReplyListByMemberNo(memberNo);

        return ResponseEntity.ok().body(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, replyList));
    }

    // 신고 처리 상태(?) 필요함. -> 미처리 분류
    // 현재는 닉네임 검색만 되게 구현 -> 추후에 { 신고 상태 } 검색 필요
    @HasAccess
    @GetMapping(value = "/reported")
    public ResponseEntity<ApiResult> getReportedReplyList(@RequestParam(defaultValue = "0")
                                                              @PositiveOrZero(message = "0 또는 자연수만 입력이 가능합니다.")
                                                              int page,
                                                          @RequestParam(defaultValue = "10")
                                                              @Positive(message = "자연수만 입력이 가능합니다.")
                                                              int size,
                                                          @RequestParam(defaultValue = "desc")
                                                              @Pattern(regexp = "desc|asc", message = "정렬 조건이 맞지 않습니다.")
                                                              String sortDirection,
                                                          @RequestParam(defaultValue = "1")
                                                              @Pattern(regexp = "^[0-2]$", message = "신고 상태 조건이 맞지 않습니다.")
                                                              int reportStatus,
                                                          @RequestParam()
                                                              @Size(max = SEARCH_MAX_LENGTH, message = SEARCH_MAX_LENGTH + "자 이내로 검색해주세요.")
                                                              @Pattern(regexp = "/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/")
                                                              String search
                                                          ) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "createDate", ProcessingStatus.fromValue(reportStatus).name());
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<GetReplyListResponse> reportedReplyPage = replyService.getReportedReplyList(
                GetReportedReplyRequest.of(pageable, search, reportStatus)
        );

        return ResponseEntity.ok().body(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, reportedReplyPage));
    }


    @PostMapping
    public ResponseEntity<ApiResult> createReply(@RequestBody CreateReplyRequest request) {

        CreateReplyResponse reply = replyService.createReply(request);

        return ResponseEntity.ok().body(ApiResult.success(CREATE_RESPONSE_MESSAGE, reply));
    }

    @PutMapping("/update/{replyNo}")
    public ResponseEntity<ApiResult> updateReply(@RequestHeader("Authorization") String bearerToken,
                                                 @PathVariable(name = "replyNo") Long replyNo,
                                                 @RequestBody(required = true) String replyContent) {

        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        UpdateReplyRequest request = UpdateReplyRequest.of(accessTokenInfo.memberNo(), accessTokenInfo.role(), replyContent);
        UpdateReplyResponse reply = replyService.updateReply(replyNo, request);

        return ResponseEntity.ok().body(ApiResult.success(UPDATE_RESPONSE_MESSAGE, reply));
    }

    @PutMapping("/delete/{replyNo}")
    public ResponseEntity<ApiResult> deleteReply(@PathVariable(name = "replyNo") Long replyNo,
                                                 @RequestHeader("Authorization") String bearerToken) {
        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        DeleteReplyRequest request = DeleteReplyRequest.of(replyNo, accessTokenInfo.role(), accessTokenInfo.memberNo());
        replyService.deleteReply(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResult.success(DELETE_RESPONSE_MESSAGE, null));
    }




}
