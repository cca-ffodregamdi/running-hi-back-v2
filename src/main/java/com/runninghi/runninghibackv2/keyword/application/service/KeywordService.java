package com.runninghi.runninghibackv2.keyword.application.service;

import com.runninghi.runninghibackv2.keyword.application.dto.request.KeywordRequest;
import com.runninghi.runninghibackv2.keyword.application.dto.response.KeywordResponse;
import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.keyword.domain.repository.KeywordDomainRepository;
import com.runninghi.runninghibackv2.keyword.domain.service.KeywordChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordDomainRepository keywordDomainRepository;
    private final KeywordChecker keywordChecker;

    @Transactional
    public KeywordResponse createKeyword(KeywordRequest request) {

        String keywordName = request.keywordName();

        keywordChecker.checkIsEmpty(keywordName);

        Keyword createdKeyword = keywordDomainRepository.save(new Keyword(keywordName));

        return new KeywordResponse(createdKeyword.getKeywordNo(), createdKeyword.getKeywordName());
    }

    @Transactional(readOnly = true)
    public KeywordResponse getKeyword(KeywordRequest request) {

        String keywordName = request.keywordName();
        Keyword getKeyword = keywordDomainRepository.findByKeywordName(keywordName)
                .orElseThrow(()->new NoSuchElementException("존재하지 않는 키워드입니다."));

        return new KeywordResponse(getKeyword.getKeywordNo(), getKeyword.getKeywordName());
    }

}
