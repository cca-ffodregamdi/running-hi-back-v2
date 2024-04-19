package com.runninghi.runninghibackv2.domain.entity;

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
@Table(name = "TBL_SCORE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreNo;

    @Column
    @Comment("달린 거리")
    private float distance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Comment("점수 소유자")
    private Member member;

    @Builder
    public Score(Long scoreNo, float distance, Member member) {
        this.scoreNo = scoreNo;
        this.distance = distance;
        this.member = member;
    }

    public void addDistance(float distance) {
        this.distance += distance;
    }
}
