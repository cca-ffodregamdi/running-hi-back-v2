package com.runninghi.runninghibackv2.reply.application.dto.response;

import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;

public record CreateReplyResponse (
        Long replyNo,

        String memberName,

        Long postNo,

        String replyContent,

        boolean isDeleted,

        Long parentNo
)
{
    public static CreateReplyResponse fromEntity (Reply reply) {
        return new CreateReplyResponse(
                reply.getReplyNo(),
                reply.getWriter().getNickname(),
                reply.getPost().getPostNo(),
                reply.getReplyContent(),
                reply.isDeleted(),
                reply.getParent().getReplyNo()
        );
    }
}
