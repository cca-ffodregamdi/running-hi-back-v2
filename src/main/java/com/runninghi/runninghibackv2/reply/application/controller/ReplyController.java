package com.runninghi.runninghibackv2.reply.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.reply.application.dto.request.CreateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.request.UpdateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.response.CreateReplyResponse;
import com.runninghi.runninghibackv2.reply.application.dto.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.reply.application.dto.response.UpdateReplyResponse;
import com.runninghi.runninghibackv2.reply.application.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/{postNo}")
    public ResponseEntity<ApiResult> getReplyList(@PathVariable(name = "postNo") Long postNo) {

        List<GetReplyListResponse> replyList =  replyService.getReplyList(postNo);

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 조회되었습니다.", replyList));
    }

    @GetMapping("byMember/{memberNo}")
    public ResponseEntity<ApiResult> getReplyListByMemberNo(@PathVariable(name = "memberNo") Long memberNo) {

        List<GetReplyListResponse> replyList = replyService.getReplyListByMemberNo(memberNo);

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 조회되었습니다.", replyList));
    }

    // 신고된 댓글들 조회 API 필요

    @PostMapping
    public ResponseEntity<ApiResult> createReply(@RequestBody CreateReplyRequest request) {

        CreateReplyResponse reply = replyService.createReply(request);

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 생성되었습니다.", reply));
    }

    @PutMapping("update/{replyNo}")
    public ResponseEntity<ApiResult> updateReply(@PathVariable(name = "replyNo") Long replyNo,
                                                 @RequestBody UpdateReplyRequest request ) {

        UpdateReplyResponse reply = replyService.updateReply(replyNo, request);
        return ResponseEntity.ok().body(ApiResult.success("성공적으로 수정되었습니다.", reply));
    }

    @PutMapping("delete/{replyNo}")
    public ResponseEntity<ApiResult> deleteReply(@PathVariable(name = "replyNo") Long replyNo,
                                                 @RequestBody Long memberNo) {
        replyService.deleteReply(replyNo, memberNo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResult.success("성공적으로 삭제되었습니다.", null));
    }




}
