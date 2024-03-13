package com.runninghi.runninghibackv2.post.domain.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode
public class GpxDataVO implements Serializable {

    @Column
    @Comment("코스 시작 위도")
    private float startLatitude;

    @Column
    @Comment("코스 시작 경도")
    private float startLongitude;

    @Column
    @Comment("코스 완료 위도")
    private float endLatitude;

    @Column
    @Comment("코스 완료 경도")
    private float endLongitude;

    @Column
    @Comment("뛴 거리")
    private float distance;

    @Column
    @Comment("뛴 시간")
    private float time;

    @Column
    @Comment("소모 칼로리")
    private float kcal;

    @Column
    @Comment("평균 속도")
    private float speed;

    @Column
    @Comment("평균 페이스 (분/km)")
    private float meanPace;

    @Column
    @Comment("평균 경사도")
    private float meanSlope;

    public GpxDataVO(float startLatitude, float startLongitude, float endLatitude, float endLongitude, float distance, float time, float kcal, float speed, float meanPace, float meanSlope) {
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.distance = distance;
        this.time = time;
        this.kcal = kcal;
        this.speed = speed;
        this.meanPace = meanPace;
        this.meanSlope = meanSlope;
    }



}
