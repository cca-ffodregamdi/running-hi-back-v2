package com.runninghi.runninghibackv2.memberpost.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Table(name = "TBL_MEMBER_POST")
public class MemberPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberPostNo;

    @ManyToOne
    @JoinColumn(name = "memberNo")
    private Member member;

    @Column
    private String memberPostTitle;

    @Column
    private String memberPostContent;

    @Column
    private String locationName;

    @Column
    private Float latitude;

    @Column
    private Float longitude;

    private MemberPost(Member member, String memberPostTitle, String memberPostContent, String locationName, Float latitude, Float longitude) {
        this.member = member;
        this.memberPostTitle = memberPostTitle;
        this.memberPostContent = memberPostContent;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static class Builder {
        private Member member;
        private String memberPostTitle;
        private String memberPostContent;
        private String locationName;
        private Float latitude;
        private Float longitude;

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder adminPostTitle(String adminPostTitle) {
            this.memberPostTitle = adminPostTitle;
            return this;
        }

        public Builder adminPostContent(String adminPostContent) {
            this.memberPostContent = adminPostContent;
            return this;
        }

        public Builder locationName(String locationName) {
            this.locationName = locationName;
            return this;
        }


        public Builder latitude(Float latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(Float longitude) {
            this.longitude = longitude;
            return this;
        }

        public MemberPost build() {
            return new MemberPost(member, memberPostTitle, memberPostContent, locationName, latitude, longitude);
        }
    }

}
