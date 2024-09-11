package com.runninghi.runninghibackv2.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.post.response.GetRecordPostResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetMonthlyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetWeeklyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.GetYearlyRecordResponse;
import com.runninghi.runninghibackv2.application.dto.record.response.RecordData;
import com.runninghi.runninghibackv2.domain.entity.Record;
import com.runninghi.runninghibackv2.domain.repository.PostQueryRepository;
import com.runninghi.runninghibackv2.domain.repository.RecordQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.runninghi.runninghibackv2.domain.entity.QRecord.record;

@Repository
@RequiredArgsConstructor
public class RecordQueryRepositoryImpl implements RecordQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostQueryRepository postQueryRepository;

    public GetWeeklyRecordResponse getWeeklyRecord(Long memberNo, LocalDate date) {
        LocalDate start = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate end = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Record> records = jpaQueryFactory.select(record)
                .from(record)
                .where(record.date.between(start, end)
                        .and(record.member.memberNo.eq(memberNo)))
                .fetch();

        Map<Integer, RecordData> weeklyRecordMap = new HashMap<>();
        int totalTime = 0;
        float totalDistance = 0.0f;
        int totalKcal = 0;

        for (Record record : records) {
            int dayOfWeekIndex = record.getDate().getDayOfWeek().getValue() - 1; // Monday=0, Sunday=6

            RecordData existingData = weeklyRecordMap.getOrDefault(dayOfWeekIndex, new RecordData(0, 0, 0, 0));
            RecordData newData = new RecordData(
                    existingData.getDistance() + record.getDistance(),
                    existingData.getTime() + record.getTime(),
                    0,
                    existingData.getKcal() + record.getKcal()
            );
            weeklyRecordMap.put(dayOfWeekIndex, newData);

            totalTime += record.getTime();
            totalDistance += record.getDistance();
            totalKcal += record.getKcal();
        }

        List<RecordData> weeklyRecordData = IntStream.range(0, 7)
                .mapToObj(i -> {
                    RecordData data = weeklyRecordMap.getOrDefault(i, new RecordData(0, 0, 0, 0));
                    int meanPace = (data.getDistance() > 0) ? Math.round(data.getTime() / data.getDistance()) : 0;
                    return new RecordData(data.getDistance(), data.getTime(), meanPace, data.getKcal());
                })
                .collect(Collectors.toList());

        int overallMeanPace = (totalDistance > 0) ? Math.round(totalTime / totalDistance) : 0;

        List<GetRecordPostResponse> postList = postQueryRepository.findWeeklyRecord(memberNo, date);

        return new GetWeeklyRecordResponse(weeklyRecordData, records.size(), totalTime, overallMeanPace, totalKcal, postList);
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
        Map<Integer, RecordData> dailyRecordMap = new HashMap<>();
        int totalTime = 0;
        float totalDistance = 0.0f;
        int totalKcal = 0;

        for (Record record : records) {
            int dayOfMonthIndex = record.getDate().getDayOfMonth() - 1;
            RecordData existingData = dailyRecordMap.getOrDefault(dayOfMonthIndex, new RecordData(0, 0, 0, 0));
            RecordData newData = new RecordData(
                    existingData.getDistance() + record.getDistance(),
                    existingData.getTime() + record.getTime(),
                    0,
                    existingData.getKcal() + record.getKcal()
            );
            dailyRecordMap.put(dayOfMonthIndex, newData);

            totalTime += record.getTime();
            totalDistance += record.getDistance();
            totalKcal += record.getKcal();
        }

        List<RecordData> dailyRecordData = IntStream.range(0, daysInMonth)
                .mapToObj(i -> {
                    RecordData data = dailyRecordMap.getOrDefault(i, new RecordData(0, 0, 0, 0));
                    int meanPace = (data.getDistance() > 0) ? Math.round(data.getTime() / data.getDistance()) : 0;
                    return new RecordData(data.getDistance(), data.getTime(), meanPace, data.getKcal());
                })
                .collect(Collectors.toList());

        int overallMeanPace = (totalDistance > 0) ? Math.round(totalTime / totalDistance) : 0;

        List<GetRecordPostResponse> postList = postQueryRepository.findMonthlyRecord(memberNo, date);

        return new GetMonthlyRecordResponse(dailyRecordData, records.size(), totalTime, overallMeanPace, totalKcal, postList);
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

        Map<Integer, RecordData> monthlyRecordMap = new HashMap<>();
        int totalTime = 0;
        float totalDistance = 0.0f;
        int totalKcal = 0;

        for (Record record : records) {
            int monthIndex = record.getDate().getMonthValue() - 1;
            RecordData existingData = monthlyRecordMap.getOrDefault(monthIndex, new RecordData(0, 0, 0, 0));
            RecordData newData = new RecordData(
                    existingData.getDistance() + record.getDistance(),
                    existingData.getTime() + record.getTime(),
                    0,
                    existingData.getKcal() + record.getKcal()
            );
            monthlyRecordMap.put(monthIndex, newData);

            totalTime += record.getTime();
            totalDistance += record.getDistance();
            totalKcal += record.getKcal();
        }

        List<RecordData> monthlyRecordData = IntStream.range(0, 12)
                .mapToObj(i -> {
                    RecordData data = monthlyRecordMap.getOrDefault(i, new RecordData(0, 0, 0, 0));
                    int meanPace = (data.getDistance() > 0) ? Math.round(data.getTime() / data.getDistance()) : 0;
                    return new RecordData(data.getDistance(), data.getTime(), meanPace, data.getKcal());
                })
                .collect(Collectors.toList());

        int overallMeanPace = (totalDistance > 0) ? Math.round(totalTime / totalDistance) : 0;

        List<GetRecordPostResponse> postList = postQueryRepository.findYearlyRecord(memberNo, date);

        return new GetYearlyRecordResponse(monthlyRecordData,  records.size(), totalTime, overallMeanPace, totalKcal, postList);
    }
}
