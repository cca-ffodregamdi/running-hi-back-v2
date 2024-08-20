package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.record.response.GetMonthlyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetWeeklyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetYearlyRecordResponse;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Record;
import com.runninghi.runninghibackv2.domain.entity.vo.GpsDataVO;
import com.runninghi.runninghibackv2.domain.repository.RecordQueryRepository;
import com.runninghi.runninghibackv2.domain.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final RecordQueryRepository recordQueryRepository;

    public void createRecord(Member member, GpsDataVO recordData) {

        Record runRecord = Record.builder()
                .member(member)
                .kcal(recordData.getKcal())
                .meanPaceSec(recordData.getMeanPace())
                .distance(recordData.getDistance())
                .time(recordData.getTime())
                .date(LocalDate.from(recordData.getRunStartTime()))
                .build();

        recordRepository.save(runRecord);
    }


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
