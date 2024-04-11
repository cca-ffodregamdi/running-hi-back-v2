package com.runninghi.runninghibackv2.post.application.dto.response;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetAllPostsResponse(
          @Schema(description = "작성자 닉네임", example = "러너1")
          String nickname,
          @Schema(description = "게시글 제목", example = "제목 예시")
          String postTitle,
          @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
          String postContent,
          @Schema(description = "권한", example = "MEMBER")
          Role role,
          @Schema(description = "코스 위치", example = "서울특별시 성북구")
          String locationName
) {
    public static GetAllPostsResponse from(Post post) {
        return new GetAllPostsResponse(
                post.getMember().getNickname(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getRole(),
                post.getLocationName()
        );
    }
}
