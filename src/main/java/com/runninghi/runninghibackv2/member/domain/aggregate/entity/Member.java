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

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    @Comment("계정 활성화 상태")
    private boolean isActive;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    @Comment("블랙리스트 상태")
    private boolean isBlacklisted;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("권한")
    private Role role;

    @Column
    @Comment("리프레시 토큰")
    private String refreshToken;

    public Member(MemberBuilder memberBuilder) {
        this.memberNo = memberBuilder.memberNo;
        this.account = memberBuilder.account;
        this.password = memberBuilder.password;
        this.nickname = memberBuilder.nickname;
        this.kakaoId = memberBuilder.kakaoId;
        this.kakaoName = memberBuilder.kakaoName;
        this.reportCnt = memberBuilder.reportCnt;
        this.isActive = memberBuilder.isActive;
        this.isBlacklisted = memberBuilder.isBlacklisted;
        this.role = memberBuilder.role;
        this.refreshToken = memberBuilder.refreshToken;
    }

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

    public static class MemberBuilder {
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
        private String refreshToken;

        public MemberBuilder memberNo(Long memberNo) {
            this.memberNo = memberNo;
            return this;
        }

        public MemberBuilder account(String account) {
            this.account = account;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public MemberBuilder kakaoId(String kakaoId) {
            this.kakaoId = kakaoId;
            return this;
        }

        public MemberBuilder kakaoName(String kakaoName) {
            this.kakaoName = kakaoName;
            return this;
        }

        public MemberBuilder reportCnt(int reportCnt) {
            this.reportCnt = reportCnt;
            return this;
        }

        public MemberBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public MemberBuilder isBlacklisted(boolean isBlacklisted) {
            this.isBlacklisted = isBlacklisted;
            return this;
        }

        public MemberBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public MemberBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Member build() {
            return new Member(this);
        }

    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
