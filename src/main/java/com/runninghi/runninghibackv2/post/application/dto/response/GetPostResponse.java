package com.runninghi.runninghibackv2.post.application.dto.response;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.keyword.postkeyword.domain.aggregate.entity.PostKeyword;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.post.domain.aggregate.vo.GpxDataVO;

import java.security.Key;
import java.util.List;

public record GetPostResponse(
        Member member,
        String postTitle,
        String postContent,
        Role role,
        String locationName,
        GpxDataVO gpxDataVO,
        List<Keyword> keywordList
) {
    public static GetPostResponse from(Post post, List<Keyword> list) {
        return new GetPostResponse(
                post.getMember(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getRole(),
                post.getLocationName(),
                post.getGpxDataVO(),
                list
        );
    }
}
