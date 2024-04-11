package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.keyword.request.KeywordRequest;
import com.runninghi.runninghibackv2.application.dto.keyword.response.KeywordResponse;
import com.runninghi.runninghibackv2.domain.entity.Keyword;
import com.runninghi.runninghibackv2.domain.repository.KeywordRepository;
import com.runninghi.runninghibackv2.domain.service.KeywordChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
