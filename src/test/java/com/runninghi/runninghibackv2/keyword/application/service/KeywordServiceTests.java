package com.runninghi.runninghibackv2.keyword.application.service;

import com.runninghi.runninghibackv2.keyword.application.dto.request.KeywordRequest;
import com.runninghi.runninghibackv2.keyword.application.dto.response.KeywordResponse;
import com.runninghi.runninghibackv2.keyword.domain.repository.KeywordDomainRepository;
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
    private KeywordDomainRepository keywordDomainRepository;

    @BeforeEach
    void clear() {
        keywordDomainRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("키워드 생성 테스트 : success")
     void testCreateKeywordSuccess() {
        // Given
        long beforeSize = keywordDomainRepository.count();
        KeywordRequest request = new KeywordRequest("TestKeyword");

        // When
        KeywordResponse response = keywordService.createKeyword(request);
        long afterSize = keywordDomainRepository.count();

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
    @DisplayName("키워드 조회 테스트 : success")
    void getKeywordSuccess() {
        // Given
        KeywordRequest request = new KeywordRequest("TestKeyword");

        // When
        keywordService.createKeyword(request);
        KeywordResponse response = keywordService.getKeyword(request);

        // Then
        assertEquals(request.keywordName(), response.keywordName());
    }

    @Test
    @DisplayName("키워드 조회 테스트 : NoSuchElement 예외처리")
    void testKeywordNoSuchElementException() {
        // Given
        KeywordRequest request = new KeywordRequest("TestKeyword");

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> keywordService.getKeyword(request));

        assertEquals("존재하지 않는 키워드입니다.", exception.getMessage());

    }

}
