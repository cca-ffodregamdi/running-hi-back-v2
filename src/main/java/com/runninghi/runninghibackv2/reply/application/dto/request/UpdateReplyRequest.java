package com.runninghi.runninghibackv2.reply.application.dto.request;

import java.time.LocalDateTime;

public record UpdateReplyRequest (

        Long memberNo,
        String replyContent
) {}
