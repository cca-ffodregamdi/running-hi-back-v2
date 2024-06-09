package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.application.dto.challenge.request.UpdateChallengeRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_CHALLENGE")
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeNo;

    @NotNull
    @Comment("챌린지명")
    private String title;

    @NotNull
    @Comment("챌린지 상세정보")
    private String content;

    //@Comment("챌린지 타입")

    @NotNull
    @Comment("목표 거리")
    private float distance;

    @Comment("목표 속도")
    private float speed;

    //@Comment("목표 출석 현황")

    @Comment("챌린지 시작일자")
    private LocalDateTime startDate;

    @Comment("챌린지 종료일자")
    private LocalDateTime endDate;

    @Comment("누적 달린 시간")
    private float runningTime;

    @Comment("누적 소모 칼로리")
    private float kcal;

    @Comment("누적 평균 페이스 (분/km)")
    private float meanPace;

    @OneToMany(mappedBy = "challenge")
    @Comment("챌린지에 참여한 멤버 리스트")
    private List<MemberChallenge> participants;

    @Builder
    public Challenge(Long challengeNo, String title, String content, float distance, LocalDateTime startDate,
                     LocalDateTime endDate, float runningTime, float kcal, float meanPace, List<MemberChallenge> participants) {
        this.challengeNo = challengeNo;
        this.title = title;
        this.content = content;
        this.distance = distance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.runningTime = runningTime;
        this.kcal = kcal;
        this.meanPace = meanPace;
        this.participants = participants;
    }

    public void update(UpdateChallengeRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.distance = request.distance();
        this.startDate = request.startDate();
        this.endDate = request.endDate();
    }
}
