package com.runninghi.runninghibackv2.post.application.dto.request;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;

public record CreatePostRequest(
        Member member,
        String postTitle,
        String postContent,
        Role role,
        String locationName
) {

}
