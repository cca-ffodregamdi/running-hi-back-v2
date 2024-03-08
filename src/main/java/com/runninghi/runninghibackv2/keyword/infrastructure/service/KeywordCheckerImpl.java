package com.runninghi.runninghibackv2.keyword.infrastructure.service;

import com.runninghi.runninghibackv2.keyword.domain.service.KeywordChecker;
import org.springframework.stereotype.Service;

@Service
public class KeywordCheckerImpl implements KeywordChecker {

    @Override
    public void checkIsEmpty(String keywordName) {
        if (keywordName.isEmpty() || keywordName.isBlank())
            throw new IllegalArgumentException("키워드는 공백일 수 없습니다.");
    }

}
