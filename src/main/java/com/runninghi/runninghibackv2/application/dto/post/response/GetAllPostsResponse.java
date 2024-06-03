package com.runninghi.runninghibackv2.application.dto.post.response;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record GetAllPostsResponse(
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
        @Schema(description = "소모 칼로리 (kcal)", example = "200")
        float kcal,
        @Schema(description = "대표 이미지 URL", example = "https://picsum.photos/200")
        String imageUrl

) {
    public static GetAllPostsResponse from(Post post, String imageUrl) {
        return new GetAllPostsResponse(
                post.getPostNo(),
                post.getCreateDate(),
                post.getPostContent(),
                post.getRole(),
                post.getMember().getNickname(),
                post.getMember().getProfileUrl(),
                post.getGpsDataVO().getKcal(),
                imageUrl

        );
    }
}

