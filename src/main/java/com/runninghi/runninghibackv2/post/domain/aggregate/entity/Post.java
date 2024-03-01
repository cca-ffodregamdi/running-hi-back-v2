package com.runninghi.runninghibackv2.post.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Member member;

    @Column
    private String postTitle;

    @Column
    private String postContent;

    @Column
    private Role role;

    @Column
    private String locationName;

    @Column
    private float startLatitude;

    @Column
    private float startLongitude;

    @Column
    private float endLatitude;

    @Column
    private float endLongitude;

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

        public Post build() {
            return new Post(this);
        }
    }

}
