package com.runninghi.runninghibackv2.post.application.dto.request;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;

import java.util.List;

public record CreatePostRequest(
        Long memberNo,
        String postTitle,
        String postContent,
        String locationName,
        List<String> keywordList
) {

}
