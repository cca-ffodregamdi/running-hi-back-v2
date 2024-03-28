package com.runninghi.runninghibackv2.post.application.dto.response;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;

public record GetAllPostsResponse(
          Member member,
          String postTitle,
          String postContent,
          Role role,
          String locationName
) {
    public static GetAllPostsResponse from(Post post) {
        return new GetAllPostsResponse(
                post.getMember(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getRole(),
                post.getLocationName()
        );
    }
}
