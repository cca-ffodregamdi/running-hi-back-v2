package com.runninghi.runninghibackv2.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.post.response.GetRecordPostResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetMonthlyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetWeeklyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetYearlyRecordResponse;
import com.runninghi.runninghibackv2.domain.entity.Record;
import com.runninghi.runninghibackv2.domain.repository.PostQueryRepository;
import com.runninghi.runninghibackv2.domain.repository.RecordQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.runninghi.runninghibackv2.domain.entity.QRecord.record;

@Repository
@RequiredArgsConstructor
public class RecordQueryRepositoryImpl implements RecordQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostQueryRepository postQueryRepository;

    @Override
    public GetWeeklyRecordResponse getWeeklyRecord(Long memberNo, LocalDate date) {
        LocalDate start = date.with(DayOfWeek.MONDAY);
        LocalDate end = date.with(DayOfWeek.SUNDAY);

        List<Record> records = jpaQueryFactory.select(record)
                .from(record)
                .where(record.date.between(start, end)
                        .and(record.member.memberNo.eq(memberNo)))
                .fetch();

        List<Float> weeklyRecordData = new ArrayList<>(Collections.nCopies(7, 0.0f));
        int totalTime = 0;
        float totalDistance = 0.0f;
        int totalKcal = 0;

        for (Record record : records) {
            int dayOfWeekIndex = record.getDate().getDayOfWeek().getValue() - 1; // Monday=0, Sunday=6
            weeklyRecordData.set(dayOfWeekIndex, weeklyRecordData.get(dayOfWeekIndex) + record.getDistance());

            totalTime += record.getTime();
            totalDistance += record.getDistance();
            totalKcal += record.getKcal();
        }

        int meanPace = (totalDistance > 0) ? Math.round(totalTime / totalDistance) * 60 : 0;

        List<GetRecordPostResponse> postList = postQueryRepository.findWeeklyRecord(memberNo, date);

        return new GetWeeklyRecordResponse(weeklyRecordData, totalTime, meanPace, totalKcal, postList);
    }

    @Override
    public GetMonthlyRecordResponse getMonthlyRecord(Long memberNo, LocalDate date) {
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());

        List<Record> records = jpaQueryFactory.select(record)
                .from(record)
                .where(record.date.between(start, end)
                        .and(record.member.memberNo.eq(memberNo)))
                .fetch();

        int daysInMonth = date.lengthOfMonth();
        List<Float> dailyRecordData = new ArrayList<>(Collections.nCopies(daysInMonth, 0.0f));
        int totalTime = 0;
        float totalDistance = 0.0f;
        int totalKcal = 0;

        for (Record record : records) {
            int dayOfMonthIndex = record.getDate().getDayOfMonth() - 1;
            dailyRecordData.set(dayOfMonthIndex, dailyRecordData.get(dayOfMonthIndex) + record.getDistance());

            totalTime += record.getTime();
            totalDistance += record.getDistance();
            totalKcal += record.getKcal();
        }

        int meanPace = (totalDistance > 0) ? Math.round(totalTime / totalDistance) * 60 : 0;

        List<GetRecordPostResponse> postList = postQueryRepository.findMonthlyRecord(memberNo, date);

        return new GetMonthlyRecordResponse(dailyRecordData, totalTime, meanPace, totalKcal, postList);
    }

    @Override
    public GetYearlyRecordResponse getYearlyRecord(Long memberNo, LocalDate date) {
        int year = date.getYear();
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        List<Record> records = jpaQueryFactory.select(record)
                .from(record)
                .where(record.date.between(start, end)
                        .and(record.member.memberNo.eq(memberNo)))
                .fetch();

        List<Float> monthlyRecordData = new ArrayList<>(Collections.nCopies(12, 0.0f));
        int totalTime = 0;
        float totalDistance = 0.0f;
        int totalKcal = 0;

        for (Record record : records) {
            int monthIndex = record.getDate().getMonthValue() - 1;
            monthlyRecordData.set(monthIndex, monthlyRecordData.get(monthIndex) + record.getDistance());

            totalTime += record.getTime();
            totalDistance += record.getDistance();
            totalKcal += record.getKcal();
        }

        int meanPace = (totalDistance > 0) ? Math.round(totalTime / totalDistance) * 60 : 0;

        List<GetRecordPostResponse> postList = postQueryRepository.findYearlyRecord(memberNo, date);

        return new GetYearlyRecordResponse(monthlyRecordData, totalTime, meanPace, totalKcal, postList);
    }
}
