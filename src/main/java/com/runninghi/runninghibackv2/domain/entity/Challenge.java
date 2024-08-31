package com.runninghi.runninghibackv2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.runninghi.runninghibackv2.application.dto.challenge.request.UpdateChallengeRequest;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeStatus;
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
    private long challengeNo;

    @NotNull
    @Comment("챌린지명")
    private String title;

    @NotNull
    @Comment("챌린지 상세정보")
    private String content;

    @NotNull
    @Comment("챌린지 타입")
    @Enumerated(value = EnumType.STRING)
    private ChallengeCategory challengeCategory;

    @NotNull
    @Comment("챌린지 이미지")
    private String imageUrl;

    @NotNull
    @Comment("목표 수치")
    private float goal;

    @Comment("목표 상세정보")
    private String goalDetail;

    @Comment("챌린지 시작일자")
    private LocalDateTime startDate;

    @Comment("챌린지 종료일자")
    private LocalDateTime endDate;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Comment("챌린지 상태")
    private ChallengeStatus status;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Comment("챌린지에 참여한 멤버 리스트")
    @JsonIgnore
    private List<MemberChallenge> participants;

    @Builder
    public Challenge(long challengeNo, String title, String content, ChallengeCategory challengeCategory,
                     String imageUrl, float goal, String goalDetail, LocalDateTime startDate, LocalDateTime endDate,
                     ChallengeStatus status, List<MemberChallenge> participants) {
        this.challengeNo = challengeNo;
        this.title = title;
        this.content = content;
        this.challengeCategory = challengeCategory;
        this.imageUrl = imageUrl;
        this.goal = goal;
        this.goalDetail = goalDetail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.participants = participants;
    }

    public void update(UpdateChallengeRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.challengeCategory = request.challengeCategory();
        this.imageUrl = request.imageUrl();
        this.goal = request.goal();
        this.goalDetail = request.goalDetail();
        this.startDate = request.startDate();
        this.endDate = request.endDate();
    }
}
