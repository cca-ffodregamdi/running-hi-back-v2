package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatePostService {

    private final PostKeywordService postKeywordService;

    public void updatePostKeyword(Post post, List<String> keywordList) {



        //기존 키워드 전체 삭제
        postKeywordService.deletePostKeyword(post.getPostNo());

        //키워드 리스트 새로 저장
        postKeywordService.createPostKeyword(post, keywordList);
    }
}
