package com.runninghi.runninghibackv2.application.dto.reply.response;

import com.runninghi.runninghibackv2.domain.entity.Reply;

import java.time.LocalDateTime;
import java.util.List;

public record GetReplyListResponse (

        Long replyNo,

        String memberName,

        Long postNo,

        String replyContent,

        boolean isDeleted,

        Reply parent,

        List<Reply> children,

        LocalDateTime createDate,

        LocalDateTime updateDate
)
{

    public static GetReplyListResponse fromEntity (Reply reply) {
        return new GetReplyListResponse(
                reply.getReplyNo(),
                reply.getWriter().getNickname(),
                reply.getPost().getPostNo(),
                reply.getReplyContent(),
                reply.isDeleted(),
                reply.getParent(),
                reply.getChildren(),
                reply.getCreateDate(),
                reply.getUpdateDate()
        );
    }

    public static GetReplyListResponse pureReplyListFromEntity (Reply reply) {
        return new GetReplyListResponse(
                reply.getReplyNo(),
                reply.getWriter().getNickname(),
                reply.getPost().getPostNo(),
                reply.getReplyContent(),
                reply.isDeleted(),
                null,
                null,
                reply.getCreateDate(),
                reply.getUpdateDate()
        );
    }

}
