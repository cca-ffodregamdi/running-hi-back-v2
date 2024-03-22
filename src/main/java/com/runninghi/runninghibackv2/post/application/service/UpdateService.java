package com.runninghi.runninghibackv2.post.application.service;

import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateService {

    private PostKeywordService postKeywordService;

    public void updatePostKeyword(Post post, List<String> keywordList) {
        //기존 키워드 전체 삭제
        postKeywordService.deletePostKeyword(post.getPostNo());

        //키워드 리스트 새로 저장
        postKeywordService.createPostKeyword(post, keywordList);
    }
}
