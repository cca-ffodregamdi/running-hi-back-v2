package com.runninghi.runninghibackv2.domain.service;

import com.runninghi.runninghibackv2.domain.entity.Keyword;
import com.runninghi.runninghibackv2.domain.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordChecker {

    private final KeywordRepository keywordRepository;

    public void checkIsEmpty(String keywordName) {
        if (keywordName.isEmpty() || keywordName.isBlank())
            throw new IllegalArgumentException("키워드는 공백일 수 없습니다.");
    }

    public Long checkExists(String keywordName) {

        Optional<Keyword> optionalKeyword = keywordRepository.findByKeywordName(keywordName);
        return optionalKeyword.map(Keyword::getKeywordNo).orElse(null);

    }

}
