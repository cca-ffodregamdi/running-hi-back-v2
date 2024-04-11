package com.runninghi.runninghibackv2.application.dto.reply.response;

import com.runninghi.runninghibackv2.domain.entity.Reply;

import java.time.LocalDateTime;

public record UpdateReplyResponse (
        Long replyNo,
        String memberName,
        Long postNo,
        String replyContent,
        boolean isDeleted,
        Long parentReplyNo,
        LocalDateTime createDate,
        LocalDateTime updateDate
)
{
    public static UpdateReplyResponse fromEntity (Reply reply) {

        if (reply.getParent() == null)
            return new UpdateReplyResponse(
                    reply.getReplyNo(),
                    reply.getWriter().getNickname(),
                    reply.getPost().getPostNo(),
                    reply.getReplyContent(),
                    reply.isDeleted(),
                    null,
                    reply.getCreateDate(),
                    reply.getUpdateDate()
            );


        return new UpdateReplyResponse(
                reply.getReplyNo(),
                reply.getWriter().getNickname(),
                reply.getPost().getPostNo(),
                reply.getReplyContent(),
                reply.isDeleted(),
                reply.getParent().getReplyNo(),
                reply.getCreateDate(),
                reply.getUpdateDate()
        );
    }

}
