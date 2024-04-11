package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.Role;

public record DeleteReplyRequest(
        Long replyNo,
        Role role,
        Long memberNo


) {
    public static DeleteReplyRequest of(Long replyNo, Role role, Long memberNo) {
        return new DeleteReplyRequest(replyNo, role, memberNo);
    }
}
