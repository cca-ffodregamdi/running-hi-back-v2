package com.runninghi.runninghibackv2.keyword.domain.service;

import org.springframework.stereotype.Service;

@Service
public class KeywordChecker {
    public void checkIsEmpty(String keywordName) {
        if (keywordName.isEmpty() || keywordName.isBlank())
            throw new IllegalArgumentException("키워드는 공백일 수 없습니다.");
    }

}
