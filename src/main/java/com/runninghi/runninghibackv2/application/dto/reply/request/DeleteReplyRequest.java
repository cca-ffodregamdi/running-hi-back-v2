package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteReplyRequest(
        @Schema(description = "삭제할 댓글 번호", example = "1")
        Long replyNo,
        @Schema(description = "요청자 직위", example = "ADMIN")
        Role role,
        @Schema(description = "요청자 회원 번호", example = "1")
        Long memberNo


) {
    public static DeleteReplyRequest of(Long replyNo, Role role, Long memberNo) {
        return new DeleteReplyRequest(replyNo, role, memberNo);
    }
}
