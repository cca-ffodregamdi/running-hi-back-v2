package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.Role;

public record UpdateReplyRequest (
    Long memberNo,
    Role role,
    String replyContent
) {
    public static UpdateReplyRequest of (Long memberNo, Role role, String replyContent) {
        return new UpdateReplyRequest(memberNo, role, replyContent);
    }
}
