package com.runninghi.runninghibackv2.reply.application.dto.request;

public record CreateReplyRequest(
        Long memberNo,
        Long postNo,
        String replyContent,
        Long parentReplyNo
) {}
