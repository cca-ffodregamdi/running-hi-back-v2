package com.runninghi.runninghibackv2.reply.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.reply.application.dto.request.CreateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.reply.application.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(name = "/{postNo}")
    public ResponseEntity<ApiResult> getReplyList(@PathVariable(name = "postNo") Long postNo) {

        List<GetReplyListResponse> replyList =  replyService.getReplyList(postNo);

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 조회되었습니다.", replyList));
    }

    @GetMapping(name = "byMember/{memberNo}")
    public ResponseEntity<ApiResult> getReplyListByMemberNo(@PathVariable(name = "memberNo") Long memberNo) {

        List<GetReplyListResponse> replyList = replyService.getReplyListByMemberNo(memberNo);

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 조회되었습니다.", replyList));
    }

    // 신고된 댓글들 조회 API 필요

    @PostMapping
    public ResponseEntity<ApiResult> createReply(@RequestBody CreateReplyRequest request) {

        replyService.createReply(request);

        return null;
    }




}
