package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.keyword.request.KeywordRequest;
import com.runninghi.runninghibackv2.application.dto.keyword.response.KeywordResponse;
import com.runninghi.runninghibackv2.domain.entity.Keyword;
import com.runninghi.runninghibackv2.domain.service.KeywordChecker;
import com.runninghi.runninghibackv2.domain.entity.PostKeyword;
import com.runninghi.runninghibackv2.domain.entity.vo.PostKeywordId;
import com.runninghi.runninghibackv2.domain.repository.PostKeywordRepository;
import com.runninghi.runninghibackv2.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostKeywordService {

    private final KeywordService keywordService;
    private final KeywordChecker keywordChecker;
    private final PostKeywordRepository postKeywordRepository;

    @Transactional
    public void createPostKeyword(Post post, List<String> keywordList) {

        List<PostKeyword> postKeywords = new ArrayList<>();

        for (String keywordName : keywordList) {
            Keyword keyword;
            PostKeyword postKeyword;

            Long keywordNo = keywordChecker.checkExists(keywordName);

            if (keywordNo == null) {
                KeywordResponse keywordResponse = keywordService.createKeyword(new KeywordRequest(keywordName));
                keyword = keywordService.findByKeywordNo(keywordResponse.keywordNo());
            } else {
                keyword = keywordService.findByKeywordNo(keywordNo);
            }

            PostKeywordId postKeywordId = PostKeywordId.builder()
                    .keywordNo(keywordNo)
                    .postNo(post.getPostNo())
                    .build();

            postKeyword = PostKeyword.builder()
                    .postKeywordId(postKeywordId)
                    .keyword(keyword)
                    .post(post)
                    .build();

            postKeywords.add(postKeyword);
        }

        postKeywordRepository.saveAll(postKeywords);
    }


    @Transactional
    public void deletePostKeyword(Long postNo) {

        postKeywordRepository.deleteAllByPostKeywordId_PostNo(postNo);

    }

    @Transactional(readOnly = true)
    public List<PostKeyword> getKeywordsByPost(Post post) {
        return postKeywordRepository.findAllByPost(post);
    }


}
