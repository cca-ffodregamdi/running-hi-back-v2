package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.reply.request.*;
import com.runninghi.runninghibackv2.application.dto.reply.response.CreateReplyResponse;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.application.dto.reply.response.UpdateReplyResponse;
import com.runninghi.runninghibackv2.application.service.ReplyService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 컨트롤러", description = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reply")
public class ReplyController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ReplyService replyService;

    private static final String GET_MAPPING_RESPONSE_MESSAGE = "성공적으로 조회되었습니다.";
    private static final String CREATE_RESPONSE_MESSAGE = "성공적으로 생성되었습니다.";
    private static final String UPDATE_RESPONSE_MESSAGE = "성공적으로 수정되었습니다.";
    private static final String DELETE_RESPONSE_MESSAGE = "성공적으로 삭제되었습니다.";

    @GetMapping("/{postNo}")
    @Operation(summary = "댓글 리스트 조회", description = "특정 게시물에 대한 댓글들 리스트를 조회합니다." )
    public ResponseEntity<ApiResult> getReplyList(@PathVariable(name = "postNo") Long postNo) {

        List<GetReplyListResponse> replyList =  replyService.getReplyList(postNo);

        return ResponseEntity.ok().body(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, replyList));
    }

    @GetMapping("/byMember")
    @Operation(summary = "특정 회원 댓글 리스트 조회", description = "특정 회원의 댓글 리스트를 조회합니다.")
    public ResponseEntity<ApiResult> getReplyListByMemberNo(@RequestHeader(name = "memberNo") Long memberNo) {

        List<GetReplyListResponse> replyList = replyService.getReplyListByMemberNo(memberNo);

        return ResponseEntity.ok().body(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, replyList));
    }

    @HasAccess
    @GetMapping(value = "/reported")
    @Operation(summary = "신고된 댓글 리스트 조회", description = "신고된 댓글 리스트를 조회합니다.")
    public ResponseEntity<ApiResult> getReportedReplyList(@ModelAttribute GetReportedReplySearchRequest searchRequest) {

        Sort sort = Sort.by( searchRequest.getSortDirection(), "createDate" );
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
        Page<GetReplyListResponse> reportedReplyPage = replyService.getReportedReplyList(
                GetReportedReplyRequest.of(pageable, searchRequest.getSearch(), searchRequest.getReportStatus())
        );

        return ResponseEntity.ok().body(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, reportedReplyPage));
    }


    @PostMapping
    @Operation(summary = "댓글 작성", description = "댓글을 작성하고, 해당 게시글 작성자와 부모 댓글이 있다면 부모 댓글 작성자에게 알림을 발송합니다.")
    public ResponseEntity<ApiResult> createReply(@RequestBody CreateReplyRequest request) {

        CreateReplyResponse response = replyService.createReply(request);

        return ResponseEntity.ok().body(ApiResult.success(CREATE_RESPONSE_MESSAGE, response));
    }

    @PutMapping("/update/{replyNo}")
    @Operation(summary = "댓글 수정", description = "특정 댓글을 수정합니다.")
    public ResponseEntity<ApiResult> updateReply(@RequestHeader("Authorization") String bearerToken,
                                                 @PathVariable(name = "replyNo") Long replyNo,
                                                 @RequestBody(required = true) String replyContent) {

        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        UpdateReplyRequest request = UpdateReplyRequest.of(accessTokenInfo.memberNo(), accessTokenInfo.role(), replyContent);
        UpdateReplyResponse reply = replyService.updateReply(replyNo, request);

        return ResponseEntity.ok().body(ApiResult.success(UPDATE_RESPONSE_MESSAGE, reply));
    }

    @PutMapping("/delete/{replyNo}")
    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    public ResponseEntity<ApiResult> deleteReply(@PathVariable(name = "replyNo") Long replyNo,
                                                 @RequestHeader("Authorization") String bearerToken) {
        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        DeleteReplyRequest request = DeleteReplyRequest.of(replyNo, accessTokenInfo.role(), accessTokenInfo.memberNo());
        replyService.deleteReply(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResult.success(DELETE_RESPONSE_MESSAGE, null));
    }

}
