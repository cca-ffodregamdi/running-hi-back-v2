package com.runninghi.runninghibackv2.reply.application.dto.request;

import com.runninghi.runninghibackv2.common.entity.Role;

public record UpdateReplyRequest (
    Long memberNo,
    Role role,
    String replyContent
) {
    public static UpdateReplyRequest of (Long memberNo, Role role, String replyContent) {
        return new UpdateReplyRequest(memberNo, role, replyContent);
    }
}
