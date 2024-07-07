package com.runninghi.runninghibackv2.application.dto.post.response;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record GetMyPostsResponse(
        @Schema(description = "게시글 번호", example = "1")
        Long postNo,
        @Schema(description = "작성 날짜", example = "2024-05-22T14:33:29")
        LocalDateTime createDate,
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "권한", example = "MEMBER")
        Role role,
        @Schema(description = "작성자 닉네임", example = "runner2424")
        String nickname,
        @Schema(description = "작성자 프로필 사진 URL", example = "https://picsum.photos/200")
        String profileImageUrl,
        @Schema(description = "대표 데이터", example = "200kcal")
        String mainData,
        @Schema(description = "대표 이미지 URL", example = "https://picsum.photos/200")
        String imageUrl,
        @Schema(description = "북마크 여부", example = "5")
        Boolean isBookmarked,
        @Schema(description = "요청자 본인의 좋아요 여부", example = "true")
        Boolean isLiked,
        @Schema(description = "댓글 개수", example = "10")
        Long replyCnt,
        @Schema(description = "좋아요 개수", example = "10")
        Long likeCnt

) {
    public static GetMyPostsResponse from(Post post, String imageUrl, Long replyCnt, Long likeCnt, Boolean isBookmarked, Boolean isLiked) {
        return new GetMyPostsResponse(
                post.getPostNo(),
                post.getCreateDate(),
                post.getPostContent(),
                post.getRole(),
                post.getMember().getNickname(),
                post.getMember().getProfileUrl(),
                post.getMainData(),
                imageUrl,
                isBookmarked,
                isLiked,
                replyCnt,
                likeCnt
        );
    }
}

