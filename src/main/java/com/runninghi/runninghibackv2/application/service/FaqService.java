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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional
    public CreateFaqResponse createFaq(CreateFaqRequest request) {
        log.info("FAQ 생성 시도.");

        Faq faq = Faq.builder()
                .question(request.question())
                .answer(request.answer())
                .build();

        Faq savedFaq = faqRepository.save(faq);
        log.info("FAQ 생성 완료. FAQ 번호: {}", savedFaq.getFaqNo());

        return CreateFaqResponse.from(faq);
    }

    @Transactional
    public UpdateFaqResponse updateFaq(Long faqNo, UpdateFaqRequest request) {
        log.info("FAQ 수정 시도. faq 번호: {}", faqNo);

        Faq faq = faqRepository.findById(faqNo)
                .orElseThrow(EntityNotFoundException::new);

        faq.update(request);
        log.info("FAQ 수정 완료. faq 번호: {}", faqNo);

        return UpdateFaqResponse.from(faq);
    }

    @Transactional(readOnly = true)
    public GetFaqResponse getFaq(Long faqNo) {
        Faq faq = findFaqById(faqNo);
        log.info("FAQ 완료. faq 번호: {}", faqNo);

        return GetFaqResponse.from(faq);
    }

    @Transactional(readOnly = true)
    public List<GetFaqResponse> getAllFaq() {
        log.info("전체 FAQ 목록 조회.");

        return faqRepository.findAll()
                .stream()
                .map(GetFaqResponse::from)
                .toList();
    }

    @Transactional
    public DeleteFaqResponse deleteFaq(Long faqNo) {
        log.info("FAQ 삭제 시도. faq 번호: {}", faqNo);
        Faq faq = findFaqById(faqNo);

        faqRepository.delete(faq);
        log.info("FAQ 삭제 완료. faq 번호: {}", faqNo);

        return DeleteFaqResponse.from(faq.getFaqNo());
    }


    private Faq findFaqById(Long faqNo) {
        return faqRepository.findById(faqNo)
                .orElseThrow(() -> {
                    log.error("해당하는 번호의 FAQ를 찾을 수 없음. faq 번호: {}", faqNo);
                    return new EntityNotFoundException();
                });
    }
}
