package com.runninghi.runninghibackv2.postreport.infrastructure.service;

import com.runninghi.runninghibackv2.member.application.service.MemberService;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.application.dto.response.GetPostResponse;
import com.runninghi.runninghibackv2.post.application.service.PostService;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
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
        // TODO member 정보 가져오기
        return null;
    }

    @Override
    public Post getPostById(Long postNo) {
        GetPostResponse postResponse = postService.getPost(postNo);
        // TODO. response Post 엔티티로 변환 필요
        Post post = Post.builder().build();
        return post;
    }
}
