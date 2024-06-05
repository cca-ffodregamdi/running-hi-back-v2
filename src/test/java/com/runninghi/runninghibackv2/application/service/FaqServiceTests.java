package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.faq.request.CreateFaqRequest;
import com.runninghi.runninghibackv2.application.dto.faq.request.UpdateFaqRequest;
import com.runninghi.runninghibackv2.application.dto.faq.response.CreateFaqResponse;
import com.runninghi.runninghibackv2.application.dto.faq.response.DeleteFaqResponse;
import com.runninghi.runninghibackv2.application.dto.faq.response.GetFaqResponse;
import com.runninghi.runninghibackv2.application.dto.faq.response.UpdateFaqResponse;
import com.runninghi.runninghibackv2.domain.entity.Faq;
import com.runninghi.runninghibackv2.domain.repository.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FaqServiceTests {

    @Autowired
    private FaqService faqService;

    @Autowired
    private FaqRepository faqRepository;

    private Long faqNo;

    @BeforeEach
    void setUp() {
        CreateFaqRequest request = new CreateFaqRequest("Test Question", "Test Answer");
        CreateFaqResponse response = faqService.createFaq(request);
        faqNo = response.faqNo();
    }

    @AfterEach
    void clear() {
        faqRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("FAQ 생성")
    void testCreateFaq() {
        CreateFaqRequest request = new CreateFaqRequest("New Question", "New Answer");
        CreateFaqResponse response = faqService.createFaq(request);

        assertNotNull(response);
        assertEquals(request.question(), response.question());
        assertEquals(request.answer(), response.answer());

        Faq faq = faqRepository.findById(response.faqNo()).orElseThrow();
        assertEquals(request.question(), faq.getQuestion());
        assertEquals(request.answer(), faq.getAnswer());
    }

    @Test
    @DisplayName("FAQ 수정")
    void testUpdateFaq() {
        UpdateFaqRequest request = new UpdateFaqRequest("Updated Question", "Updated Answer");
        UpdateFaqResponse response = faqService.updateFaq(faqNo, request);

        assertNotNull(response);
        assertEquals(request.question(), response.question());
        assertEquals(request.answer(), response.answer());

        Faq updatedFaq = faqRepository.findById(faqNo).orElseThrow();
        assertEquals(request.question(), updatedFaq.getQuestion());
        assertEquals(request.answer(), updatedFaq.getAnswer());
    }

    @Test
    @DisplayName("FAQ 수정 : 존재하지않는 faq 수정 시 예외처리")
    void testUpdateFaq_NotFound() {
        UpdateFaqRequest request = new UpdateFaqRequest("Updated Question", "Updated Answer");

        assertThrows(EntityNotFoundException.class, () -> {
            faqService.updateFaq(Long.MAX_VALUE, request);
        });
    }

    @Test
    @DisplayName("FAQ 조회")
    void testGetFaq() {
        GetFaqResponse response = faqService.getFaq(faqNo);

        assertNotNull(response);
        assertEquals("Test Question", response.question());
        assertEquals("Test Answer", response.answer());
    }

    @Test
    @DisplayName("FAQ 조회 : 존재하지않는 faq 조회 시 예외처리")
    void testGetFaq_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            faqService.getFaq(Long.MAX_VALUE);
        });
    }

    @Test
    @DisplayName("FAQ 리스트 조회")
    void testGetAllFaq() {
        List<GetFaqResponse> faqResponses = faqService.getAllFaq();

        assertNotNull(faqResponses);
        assertFalse(faqResponses.isEmpty());
        assertEquals(1, faqResponses.size());
        assertEquals("Test Question", faqResponses.get(0).question());
        assertEquals("Test Answer", faqResponses.get(0).answer());
    }

    @Test
    @DisplayName("FAQ 삭제")
    void testDeleteFaq() {
        DeleteFaqResponse response = faqService.deleteFaq(faqNo);

        assertNotNull(response);
        assertEquals(faqNo, response.faqNo());

        assertFalse(faqRepository.findById(faqNo).isPresent());
    }

    @Test
    @DisplayName("FAQ 삭제 : 존재하지않는 faq 삭제 시 예외처리")
    void testDeleteFaq_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            faqService.deleteFaq(Long.MAX_VALUE);
        });
    }
}
