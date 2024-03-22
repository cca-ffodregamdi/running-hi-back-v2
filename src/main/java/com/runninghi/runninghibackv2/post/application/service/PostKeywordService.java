package com.runninghi.runninghibackv2.post.application.service;

import com.runninghi.runninghibackv2.keyword.application.dto.request.KeywordRequest;
import com.runninghi.runninghibackv2.keyword.application.dto.response.KeywordResponse;
import com.runninghi.runninghibackv2.keyword.application.service.KeywordService;
import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.keyword.domain.service.KeywordChecker;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.PostKeyword;
import com.runninghi.runninghibackv2.post.domain.aggregate.vo.PostKeywordId;
import com.runninghi.runninghibackv2.post.domain.repository.PostKeywordRepository;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
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

//    @Transactional
    public void updatePostKeyword(Post post, List<String> keywordList) {

        /* 추후 수정사항
        * Spring의 @Async 또는 @Transactional 어노테이션이 지정된 메소드는 해당 클래스 내에서 직접 호출되어서는 안 됩니다.
        * @Transactional 어노테이션이 지정된 메소드가 직접 호출될 경우,
        * 스프링은 트랜잭션을 시작하거나 커밋하는 등의 관련된 작업을 수행하지 않습니다.
        * 이로 인해 데이터베이스 트랜잭션의 일관성이 깨질 수 있기 때문입니다. */

        //기존 키워드 전체 삭제
        deletePostKeyword(post.getPostNo());

        //키워드 리스트 새로 저장
        createPostKeyword(post, keywordList);
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
