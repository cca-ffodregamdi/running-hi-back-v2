package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.keyword.request.KeywordRequest;
import com.runninghi.runninghibackv2.application.dto.keyword.response.KeywordResponse;
import com.runninghi.runninghibackv2.domain.repository.KeywordRepository;
import com.runninghi.runninghibackv2.domain.repository.PostKeywordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class KeywordServiceTests {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @BeforeEach
    void clear() {
        postKeywordRepository.deleteAllInBatch();
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
        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> keywordService.findByKeywordNo(999L));

        assertEquals("해당 키워드가 존재하지 않습니다.", exception.getMessage());
    }


}
