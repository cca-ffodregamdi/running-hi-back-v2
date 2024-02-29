package com.runninghi.runninghibackv2.adminpost.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Table(name = "TBL_ADMIN_POST")
public class AdminPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminPostNo;

    @ManyToOne
    @JoinColumn(name = "memberNo")
    private Member admin;

    @Column
    private String adminPostTitle;

    @Column
    private String adminPostContent;

    @Column
    private Float latitude;

    @Column
    private Float longitude;

    private AdminPost(Member admin, String adminPostTitle, String adminPostContent, Float latitude, Float longitude) {
        this.admin = admin;
        this.adminPostTitle = adminPostTitle;
        this.adminPostContent = adminPostContent;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static class Builder {
        private Member admin;
        private String adminPostTitle;
        private String adminPostContent;
        private Float latitude;
        private Float longitude;

        public Builder admin(Member admin) {
            this.admin = admin;
            return this;
        }

        public Builder adminPostTitle(String adminPostTitle) {
            this.adminPostTitle = adminPostTitle;
            return this;
        }

        public Builder adminPostContent(String adminPostContent) {
            this.adminPostContent = adminPostContent;
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

        public AdminPost build() {
            return new AdminPost(admin, adminPostTitle, adminPostContent, latitude, longitude);
        }
    }

}
