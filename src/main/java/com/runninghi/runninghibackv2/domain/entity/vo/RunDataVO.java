package com.runninghi.runninghibackv2.domain.entity.vo;

import com.runninghi.runninghibackv2.common.converter.FloatListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class RunDataVO {

    @Column
    @Comment("누적 거리")
    private double totalDistance = 0.0;

    @Column
    @Comment("누적 칼로리")
    private double totalKcal = 0.0;

    @Column
    @Comment("다음 레벨에 필요한 거리")
    private int distanceToNextLevel = 10;

    @Column
    @Comment("누적거리에 따른 레벨")
    private int level = 0;

    @Column
    @Convert(converter = FloatListConverter.class)
    @Comment("주간 러닝 기록 (월~일)")
    private List<Float> weeklyRunData;

    @Column
    @Convert(converter = FloatListConverter.class)
    @Comment("월간 러닝 기록 (1월~12월)")
    private List<Float> monthlyRunData;

    @Column
    @Convert(converter = FloatListConverter.class)
    @Comment("연간 러닝 기록 (2024년~)")
    private List<Float> yearlyRunData;

    @Builder
    public RunDataVO(double totalDistance, double totalKcal, int distanceToNextLevel, int level, List<Float> weeklyRunData, List<Float> monthlyRunData, List<Float> yearlyRunData) {
        this.totalDistance = totalDistance;
        this.totalKcal = totalKcal;
        this.distanceToNextLevel = distanceToNextLevel;
        this.level = level;
        this.weeklyRunData = weeklyRunData;
        this.monthlyRunData = monthlyRunData;
        this.yearlyRunData = yearlyRunData;
    }

    // 누적 거리 업데이트
    public void updateTotalDistanceAndLevel(double distance) {
        this.totalDistance += distance;
        checkAndLevelUp();
    }

    // 레벨 업데이트를 확인하고 처리
    private void checkAndLevelUp() {
        while (this.totalDistance >= this.distanceToNextLevel) {
            this.level++;
            this.distanceToNextLevel = calculateDistanceForNextLevel();
        }
    }

    // 다음 레벨까지의 거리 업데이트
    // 이전 레벨업에 필요했던 거리의 1.5배로 계산하되, 10 단위로 떨어지지않을 경우 올림 처리
    private int calculateDistanceForNextLevel() {
        int result = (int)(distanceToNextLevel * 1.5);

        if (result % 10 != 0) {
            result = result + (10 - result % 10);
        }

        return result;
    }
    public void cleanupDeactivateMemberRunData() {
        this.totalDistance = 0;
        this.totalKcal = 0;
        this.distanceToNextLevel = 0;
        this.level = 0;
    }
    public void increaseRunData(float distance) {

    }
}
