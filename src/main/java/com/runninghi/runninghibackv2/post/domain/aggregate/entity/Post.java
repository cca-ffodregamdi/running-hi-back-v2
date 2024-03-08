package com.runninghi.runninghibackv2.post.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_POST")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @ManyToOne
    @JoinColumn(name = "memberNo")
    @Comment("작성자 계정 아이디")
    private Member member;

    @Column
    @Comment("게시글 제목")
    private String postTitle;

    @Column
    @Comment("게시글 내용")
    private String postContent;

    @Column
    @Comment("권한")
    private Role role;

    @Column
    @Comment("지역명")
    private String locationName;

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

    private Post(Builder builder) {
        this.member = builder.member;
        this.postTitle = builder.postTitle;
        this.postContent = builder.postContent;
        this.role = builder.role;
        this.locationName = builder.locationName;
        this.startLatitude = builder.startLatitude;
        this.startLongitude = builder.startLongitude;
        this.endLatitude = builder.endLatitude;
        this.endLongitude = builder.endLongitude;
        this.distance = builder.distance;
        this.time = builder.time;
        this.kcal = builder.kcal;
        this.speed = builder.speed;
        this.meanPace = builder.meanPace;
        this.meanSlope = builder.meanSlope;
    }


    public static class Builder {
        private Member member;
        private String postTitle;
        private String postContent;
        private Role role;
        private String locationName;
        private float startLatitude;
        private float startLongitude;
        private float endLatitude;
        private float endLongitude;
        private float distance;
        private float time;
        private float kcal;
        private float speed;
        private float meanPace;
        private float meanSlope;

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder postTitle(String postTitle) {
            this.postTitle = postTitle;
            return this;
        }

        public Builder postContent(String postContent) {
            this.postContent = postContent;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder locationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public Builder startLatitude(float startLatitude) {
            this.startLatitude = startLatitude;
            return this;
        }

        public Builder startLongitude(float startLongitude) {
            this.startLongitude = startLongitude;
            return this;
        }

        public Builder endLatitude(float endLatitude) {
            this.endLatitude = endLatitude;
            return this;
        }

        public Builder endLongitude(float endLongitude) {
            this.endLongitude = endLongitude;
            return this;
        }

        public Builder distance(float distance) {
            this.distance = distance;
            return this;
        }

        public Builder time(float time) {
            this.time = time;
            return this;
        }

        public Builder kcal(float kcal) {
            this.kcal = kcal;
            return this;
        }

        public Builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder meanPace(float meanPace) {
            this.meanPace = meanPace;
            return this;
        }

        public Builder meanSlope(float meanSlope) {
            this.meanSlope = meanSlope;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }

}
