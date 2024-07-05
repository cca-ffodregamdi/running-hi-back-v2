package com.runninghi.runninghibackv2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.runninghi.runninghibackv2.application.dto.challenge.request.UpdateChallengeRequest;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;
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

    @NotNull
    @Comment("챌린지 타입")
    @Enumerated(value = EnumType.STRING)
    private ChallengeCategory challengeCategory;

    @NotNull
    @Comment("챌린지 이미지")
    private String imageUrl;

    @NotNull
    @Comment("목표 수치")
    private String goal;

    @Comment("챌린지 시작일자")
    private LocalDateTime startDate;

    @Comment("챌린지 종료일자")
    private LocalDateTime endDate;

    @Comment("챌린지 활성화 상태")
    private boolean status;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Comment("챌린지에 참여한 멤버 리스트")
    @JsonIgnore
    private List<MemberChallenge> participants;

    @Builder
    public Challenge(Long challengeNo, String title, String content, ChallengeCategory challengeCategory,
                     String imageUrl, String goal, LocalDateTime startDate, LocalDateTime endDate,
                     boolean status, List<MemberChallenge> participants) {
        this.challengeNo = challengeNo;
        this.title = title;
        this.content = content;
        this.challengeCategory = challengeCategory;
        this.imageUrl = imageUrl;
        this.goal = goal;
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
        this.startDate = request.startDate();
        this.endDate = request.endDate();
    }
}
