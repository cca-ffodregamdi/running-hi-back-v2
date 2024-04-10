package com.runninghi.runninghibackv2.reply.infrastructure.service;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.service.MemberService;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.post.application.service.PostService;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.reply.domain.service.ApiReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiReplyServiceImpl implements ApiReplyService {

    private final MemberService memberService;
    private final PostService postService;

    @Override
    public Member getMemberByMemberNo(Long memberNo) {
        // Member 엔티티 가져옴.
        return null;
    }

    @Override
    public Post getPostByPostNo(Long postNo) {
        // Post 엔티티 가져옴.
        return null;
    }

    @Override
    public Role getMemberRoleByMemberNo(Long memberNo) {
        // Member의 role을 가져옴.
        return null;
    }
}
