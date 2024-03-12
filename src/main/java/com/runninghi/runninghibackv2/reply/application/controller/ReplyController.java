package com.runninghi.runninghibackv2.reply.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.reply.application.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(name = "/{postNo}")
    public ResponseEntity<ApiResult> getReplyList(@PathVariable(name = "postNo") Long postNo) {

        replyService.getReplyList(postNo);

        return null;
    }


}
