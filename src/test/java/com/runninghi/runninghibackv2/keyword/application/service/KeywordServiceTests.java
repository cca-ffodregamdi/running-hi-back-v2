package com.runninghi.runninghibackv2.keyword.application.service;

import com.runninghi.runninghibackv2.keyword.application.dto.request.KeywordRequest;
import com.runninghi.runninghibackv2.keyword.application.dto.response.KeywordResponse;
import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.keyword.domain.repository.KeywordRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class KeywordServiceTests {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private KeywordRepository keywordRepository;

    @BeforeEach
    void clear() {
        keywordRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("키워드 생성 테스트 : success")
     void testCreateKeywordSuccess() {
        // Given
        long beforeSize = keywordRepository.count();
        KeywordRequest request = new KeywordRequest("TestKeyword");

        // When
        KeywordResponse response = keywordService.createKeyword(request);
        long afterSize = keywordRepository.count();

        // Then
        assertEquals(request.keywordName(), response.keywordName());
        assertEquals(beforeSize + 1, afterSize);
    }

    @Test
    @DisplayName("키워드 생성 테스트 : 공백 예외처리")
    void testKeywordIsNullException() {
        // Given
        KeywordRequest request = new KeywordRequest(" ");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> keywordService.createKeyword(request));

        assertEquals("키워드는 공백일 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("키워드 조회 테스트 : NoSuchElement 예외처리")
    void testKeywordNoSuchElementException() {
        // Given
        Keyword keyword = new Keyword("테스트");

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> keywordService.findByKeywordNo(keyword.getKeywordNo()));

        assertEquals("존재하지 않는 키워드입니다.", exception.getMessage());
    }


}
