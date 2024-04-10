package com.runninghi.runninghibackv2.post.infrastructure.service;

import com.runninghi.runninghibackv2.application.service.MemberService;
import com.runninghi.runninghibackv2.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.service.ApiPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiPostServiceImpl implements ApiPostService {

    private final MemberService memberService;

    @Override
    public Member getMemberById(Long memberNo) {
        return memberService.findMemberByNo(memberNo);
    }
}
