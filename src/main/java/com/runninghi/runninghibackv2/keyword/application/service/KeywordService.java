package com.runninghi.runninghibackv2.keyword.application.service;

import com.runninghi.runninghibackv2.keyword.application.dto.request.KeywordRequest;
import com.runninghi.runninghibackv2.keyword.application.dto.response.KeywordResponse;
import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.keyword.domain.repository.KeywordRepository;
import com.runninghi.runninghibackv2.keyword.domain.service.KeywordChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final KeywordChecker keywordChecker;

    @Transactional
    public KeywordResponse createKeyword(KeywordRequest request) {

        String keywordName = request.keywordName();

        keywordChecker.checkIsEmpty(keywordName);

        Keyword createdKeyword = keywordRepository.save(new Keyword(keywordName));

        return new KeywordResponse(createdKeyword.getKeywordNo(), createdKeyword.getKeywordName());
    }

    @Transactional(readOnly = true)
    public Keyword findByKeywordNo(Long keywordNo) {
        return keywordRepository.findById(keywordNo)
                .orElseThrow(() -> new EntityNotFoundException("해당 키워드가 존재하지 않습니다."));
    }

}
