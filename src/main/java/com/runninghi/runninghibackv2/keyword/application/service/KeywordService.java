package com.runninghi.runninghibackv2.keyword.application.service;

import com.runninghi.runninghibackv2.keyword.application.dto.request.CreateKeywordRequest;
import com.runninghi.runninghibackv2.keyword.application.dto.response.CreateKeywordResponse;
import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.keyword.domain.repository.KeywordDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordDomainRepository keywordDomainRepository;

    @Transactional
    public CreateKeywordResponse createKeyword(CreateKeywordRequest request) {

        String keywordName = request.keywordName();

        System.out.println("keywordName = " + keywordName);
        Keyword createdKeyword = keywordDomainRepository.save(new Keyword(keywordName));

        return new CreateKeywordResponse(createdKeyword.getKeywordNo(), createdKeyword.getKeywordName());
    }

}
