package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.record.response.GetMonthlyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetWeeklyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetYearlyRecordResponse;

import java.time.LocalDate;

public interface RecordQueryRepository {
    GetWeeklyRecordResponse getWeeklyRecord(Long memberNo, LocalDate date);
    GetMonthlyRecordResponse getMonthlyRecord(Long memberNo, LocalDate date);
    GetYearlyRecordResponse getYearlyRecord(Long memberNo, LocalDate date);
}
