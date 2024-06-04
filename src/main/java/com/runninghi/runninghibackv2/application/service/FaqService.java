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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional
    public CreateFaqResponse createFaq(CreateFaqRequest request) {
        Faq faq = Faq.builder()
                .question(request.question())
                .answer(request.answer())
                .build();

        faqRepository.save(faq);

        return CreateFaqResponse.from(faq);
    }

    @Transactional
    public UpdateFaqResponse updateNotice(Long faqNo, UpdateFaqRequest request) {
        Faq faq = faqRepository.findById(faqNo)
                .orElseThrow(EntityNotFoundException::new);

        faq.update(request);

        return UpdateFaqResponse.from(faq);
    }

    @Transactional(readOnly = true)
    public GetFaqResponse getFaq(Long faqNo) {
        Faq faq = faqRepository.findById(faqNo)
                .orElseThrow(EntityNotFoundException::new);

        return GetFaqResponse.from(faq);
    }

    @Transactional(readOnly = true)
    public List<GetFaqResponse> getAllFaqs() {
        return faqRepository.findAll()
                .stream()
                .map(GetFaqResponse::from)
                .toList();
    }

    @Transactional
    public DeleteFaqResponse deleteFaq(Long faqNo) {
        Faq faq = faqRepository.findById(faqNo)
                .orElseThrow(EntityNotFoundException::new);

        faqRepository.delete(faq);

        return DeleteFaqResponse.from(faq.getFaqNo());
    }
}
