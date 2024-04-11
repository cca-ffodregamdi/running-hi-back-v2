package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.application.dto.reply.request.GetReportedReplySearchRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.CreateReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.DeleteReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.GetReportedReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.UpdateReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.response.CreateReplyResponse;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.application.dto.reply.response.UpdateReplyResponse;
import com.runninghi.runninghibackv2.application.service.ReplyService;
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

    @HasAccess
    @GetMapping(value = "/reported")
    public ResponseEntity<ApiResult> getReportedReplyList(@ModelAttribute GetReportedReplySearchRequest searchRequest) {

        Sort sort = Sort.by( searchRequest.getSortDirection(), "createDate" );
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
        Page<GetReplyListResponse> reportedReplyPage = replyService.getReportedReplyList(
                GetReportedReplyRequest.of(pageable, searchRequest.getSearch(), searchRequest.getReportStatus())
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
