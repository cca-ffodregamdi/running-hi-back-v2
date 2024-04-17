package com.runninghi.runninghibackv2.application.dto.reply.response;

import com.runninghi.runninghibackv2.domain.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record GetReplyListResponse (

        @Schema(description = "댓글 번호", example = "2")
        Long replyNo,
        @Schema(description = "댓글 작성자 닉네임", example = "러너1")
        String memberName,
        @Schema(description = "게시글 번호", example = "1")
        Long postNo,
        @Schema(description = "댓글 내용", example = "댓글 내용")
        String replyContent,
        @Schema(description = "댓글 삭제 여부", example = "false")
        boolean isDeleted,
        @Schema(description = "부모 댓글", example = "")
        Reply parent,
        @Schema(description = "자식 댓글 리스트", example = "")
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
