package com.runninghi.runninghibackv2.member.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.common.entity.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "TBL_MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column
    @Comment("계정 아이디")
    private String account;

    @Column
    @Comment("계정 비밀번호")
    private String password;

    @Column(nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Column
    @Comment("카카오 로그인 : id")
    private String kakaoId;

    @Column
    @Comment("카카오 로그인 : 카카오에 설정된 이름")
    private String kakaoName;

    @Column
    @Comment("신고된 횟수")
    private int reportCnt;

    @Column(nullable = false)
    @Comment("계정 활성화 상태")
    private boolean isActive = true;

    @Column(nullable = false)
    @Comment("블랙리스트 상태")
    private boolean isBlacklisted = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("권한")
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
        private Long memberNo;
        private String account;
        private String password;
        private String nickname;
        private String kakaoId;
        private String kakaoName;
        private int reportCnt;
        private boolean isActive;
        private boolean isBlacklisted;
        private Role role;

        public Builder memberNo(Long memberNo) {
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
