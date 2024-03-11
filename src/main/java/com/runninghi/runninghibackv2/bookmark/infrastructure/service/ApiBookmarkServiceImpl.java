package com.runninghi.runninghibackv2.bookmark.infrastructure.service;

import com.runninghi.runninghibackv2.bookmark.domain.service.ApiBookmarkService;
import com.runninghi.runninghibackv2.member.application.service.MemberService;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiBookmarkServiceImpl implements ApiBookmarkService {

    private final MemberService memberService;
//    private final  PostService postService;


    @Override
    public Member getMemberById(Long memberNo) {
//        Member member = memberService.getMemberById(memberNo);
        return null;
    }

    @Override
    public Post getPostById(Long aLong) {

        return null;
    }
}
