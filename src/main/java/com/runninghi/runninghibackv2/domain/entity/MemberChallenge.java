package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.entity.vo.GpsDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_MEMBER_CHALLENGE")
public class MemberChallenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberChallengeId;

    @ManyToOne
    @JoinColumn(name = "CHALLENGE_ID")
    @Comment("멤버가 참여한 챌린지")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    @Comment("챌린지에 참여한 멤버")
    private Member member;

    @Comment("챌린지 시작 후 달린 거리")
    private float distance;

    @Comment("챌린지 시작 후 달린 시간")
    private float runningTime;

    @Comment("챌린지 시작 후 소모 칼로리")
    private float kcal;

    @Comment("챌린지 시작 후 평균 속도")
    private float speed;

    @Comment("챌린지 시작 후 평균 페이스 (분/km)")
    private float meanPace;

    @Comment("챌린지 달성 여부")
    private boolean status;

    @Builder
    public MemberChallenge(Long memberChallengeId, Challenge challenge, Member member, float distance, float runningTime,
                           float kcal, float speed, float meanPace, boolean status) {
        this.memberChallengeId = memberChallengeId;
        this.challenge = challenge;
        this.member = member;
        this.distance = distance;
        this.runningTime = runningTime;
        this.kcal = kcal;
        this.speed = speed;
        this.meanPace = meanPace;
        this.status = status;
    }

    public void updateRecord(GpsDataVO gpsDataVO) {
        this.distance += gpsDataVO.getDistance();
        this.runningTime += gpsDataVO.getTime();
        this.kcal += gpsDataVO.getKcal();
        this.speed = this.speed == 0 ? gpsDataVO.getSpeed() : (speed + gpsDataVO.getSpeed()) / 2;
        this.meanPace = this.meanPace == 0 ? gpsDataVO.getMeanPace() : (meanPace + gpsDataVO.getMeanPace()) / 2;

        checkAndUpdateStatus(this.challenge.getChallengeCategory(), this.challenge.getTargetValue());
    }

    private void checkAndUpdateStatus(ChallengeCategory challengeCategory, float targetValue) {
        float value = 0;
        if(challengeCategory == ChallengeCategory.DISTANCE) value = this.distance;
        if(challengeCategory == ChallengeCategory.SPEED) value = this.speed;
        // if(challengeCategory == ChallengeCategory.ATTENDANCE)

        this.status = value >= targetValue ? true : false;
    }
}
