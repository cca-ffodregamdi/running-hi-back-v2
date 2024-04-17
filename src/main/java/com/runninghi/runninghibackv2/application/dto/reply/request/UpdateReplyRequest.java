package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateReplyRequest (

    @Schema(description = "회원 번호", example = "1")
    Long memberNo,
    @Schema(description = "회원 등급", example = "USER")
    Role role,
    @Schema(description = "변경할 댓글 내용", example = "변경된 댓글 내용")
    String replyContent
) {
    public static UpdateReplyRequest of (Long memberNo, Role role, String replyContent) {
        return new UpdateReplyRequest(memberNo, role, replyContent);
    }
}
