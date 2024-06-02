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
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeNo;

    @NotNull
    @Comment("챌린지명")
    private String title;

    @NotNull
    @Comment("챌린지 상세정보")
    private String content;

    @NotNull
    @Comment("목표 거리")
    private float distance;

    @Comment("챌린지 시작일자")
    private LocalDateTime startDate;

    @Comment("챌린지 종료일자")
    private LocalDateTime endDate;

    @Comment("달성 보상")
    private String reward;  // 보상 종류, 타입 수정

    @OneToMany(mappedBy = "challenge")
    @Comment("챌린지에 참여한 멤버")
    private List<MemberChallenge> participants;

    @Builder
    public Challenge(Long challengeNo, String title, String content, float distance, LocalDateTime startDate,
                     LocalDateTime endDate, String reward, List<MemberChallenge> participants) {
        this.challengeNo = challengeNo;
        this.title = title;
        this.content = content;
        this.distance = distance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reward = reward;
        this.participants = participants;
    }

    public void update(UpdateChallengeRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.distance = request.distance();
        this.startDate = request.startDate();
        this.endDate = request.endDate();
        this.reward = request.reward();
    }
}
