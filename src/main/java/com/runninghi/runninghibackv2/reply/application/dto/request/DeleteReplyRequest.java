package com.runninghi.runninghibackv2.reply.application.dto.request;

import com.runninghi.runninghibackv2.common.entity.Role;

public record DeleteReplyRequest(
        Long replyNo,
        Role role,
        Long memberNo


) {
    public static DeleteReplyRequest of(Long replyNo, Role role, Long memberNo) {
        return new DeleteReplyRequest(replyNo, role, memberNo);
    }
}
