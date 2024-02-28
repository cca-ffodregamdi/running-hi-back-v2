package com.runninghi.runninghibackv2.member.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "TBL_MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID memberNo;

    @Column
    private String account;

    @Column
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String kakaoId;

    @Column
    private String kakaoName;

    @Column
    private int reportCnt;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isBlacklisted = false;

    @Column(nullable = false)
    private Role role;

    public Member(Builder builder) {
        this.memberNo = builder.memberNo;
        this.account = builder.account;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.kakaoId = builder.kakaoId;
        this.kakaoName = builder.kakaoName;
        this.reportCnt = builder.reportCnt;
        this.isActive = builder.isActive;
        this.isBlacklisted = builder.isBlacklisted;
        this.role = builder.role;
    }

    public static class Builder {
        private UUID memberNo;
        private String account;
        private String password;
        private String nickname;
        private String kakaoId;
        private String kakaoName;
        private int reportCnt;
        private boolean isActive;
        private boolean isBlacklisted;
        private Role role;

        public Builder memberNo(UUID memberNo) {
            this.memberNo = memberNo;
            return this;
        }

        public Builder account(String account) {
            this.account = account;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder kakaoId(String kakaoId) {
            this.kakaoId = kakaoId;
            return this;
        }

        public Builder kakaoName(String kakaoName) {
            this.kakaoName = kakaoName;
            return this;
        }

        public Builder reportCnt(int reportCnt) {
            this.reportCnt = reportCnt;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder isBlacklisted(boolean isBlacklisted) {
            this.isBlacklisted = isBlacklisted;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Member build() {
            return new Member(this);
        }

    }


}
