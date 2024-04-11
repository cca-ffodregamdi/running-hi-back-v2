package com.runninghi.runninghibackv2.postreport.infrastructure.service;

import com.runninghi.runninghibackv2.application.service.MemberService;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.application.dto.post.response.GetPostResponse;
import com.runninghi.runninghibackv2.application.service.PostService;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.postreport.domain.service.ApiPostReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiPostReportServiceImpl implements ApiPostReportService {

    private final MemberService memberService;
    private final PostService postService;


    @Override
    public Member getMemberById(Long memberNo) {

        return memberService.findMemberByNo(memberNo);
    }

    @Override
    public Post getPostById(Long postNo) {
        GetPostResponse postResponse = postService.getPost(postNo);
        // TODO. response Post 엔티티로 변환 필요

        return null;
    }

    @Override
    public void deletePostById(Long postNo) {
        postService.deleteReportedPost(postNo);
    }

    @Override
    public void addReportedCountToMember(Long memberNo) {
        memberService.addReportedCount(memberNo);
    }

    @Override
    public void addReportedCountToPost(Long postNo) {
        postService.addReportedCount(postNo);
    }

    @Override
    public void resetReportedCountOfPost(Long postNo) {
        postService.resetReportedCount(postNo);
    }
}
