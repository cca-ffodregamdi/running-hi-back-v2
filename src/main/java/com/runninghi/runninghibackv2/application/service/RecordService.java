package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.record.response.GetMonthlyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetWeeklyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetYearlyRecordResponse;
import com.runninghi.runninghibackv2.domain.repository.RecordQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordQueryRepository recordQueryRepository;
    public GetWeeklyRecordResponse getWeeklyRecord(Long memberNo, LocalDate date) {
        return recordQueryRepository.getWeeklyRecord(memberNo, date);
    }

    public GetMonthlyRecordResponse getMonthlyRecord(Long memberNo, LocalDate date) {
        return recordQueryRepository.getMonthlyRecord(memberNo, date);
    }

    public GetYearlyRecordResponse getYearlyRecord(Long memberNo, LocalDate date) {
        return recordQueryRepository.getYearlyRecord(memberNo, date);
    }
}
