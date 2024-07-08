package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.entity.vo.GpsDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Comment("챌린지에 참여한 멤버")
    private Member member;

    @Comment("챌린지 시작 후 누적 기록")
    private float record;

    @Builder
    public MemberChallenge(Long memberChallengeId, Challenge challenge, Member member) {
        this.memberChallengeId = memberChallengeId;
        this.challenge = challenge;
        this.member = member;
        this.record = 0;
    }

    public void updateRecord(GpsDataVO gpsDataVO) {
        ChallengeCategory challengeCategory = this.challenge.getChallengeCategory();
        float floatRecord = this.record;

        if(challengeCategory == ChallengeCategory.DISTANCE) {
//            this.record = String.valueOf(floatRecord + gpsDataVO.getDistance());
            this.record = floatRecord + gpsDataVO.getDistance();
        }

        if(challengeCategory == ChallengeCategory.SPEED) {
//            floatRecord = floatRecord == 0 ? gpsDataVO.getMeanPace() : (floatRecord + gpsDataVO.getMeanPace()) / 2;
//            this.record = String.valueOf(floatRecord);
        }
    }

    public void updateRecord() {
//        int intRecord = Integer.parseInt(this.record);
//
//        this.record = String.valueOf(intRecord + 1);
    }
}
