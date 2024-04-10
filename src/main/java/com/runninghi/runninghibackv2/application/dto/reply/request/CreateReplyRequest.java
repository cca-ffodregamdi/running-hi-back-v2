package com.runninghi.runninghibackv2.application.dto.reply.request;

public record CreateReplyRequest(
        Long memberNo,
        Long postNo,
        String replyContent,
        Long parentReplyNo
) {}
