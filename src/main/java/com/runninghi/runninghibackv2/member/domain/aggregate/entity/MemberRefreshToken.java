package com.runninghi.runninghibackv2.member.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "TBL_REFRESH_TOKEN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRefreshToken {

    @Id
    private Long memberNo;

    @Column
    private String refreshToken;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    public MemberRefreshToken(MemberRefreshTokenBuilder memberRefreshTokenBuilder) {
        this.memberNo = memberRefreshTokenBuilder.memberNo;
        this.refreshToken = memberRefreshTokenBuilder.refreshToken;
        this.member = memberRefreshTokenBuilder.member;
    }

    public static MemberRefreshTokenBuilder builder() {
        return new MemberRefreshTokenBuilder();
    }

    public static class MemberRefreshTokenBuilder {
        private Long memberNo;
        private String refreshToken;
        private Member member;

        public MemberRefreshTokenBuilder memberNo(Long memberNo) {
            this.memberNo = memberNo;
            return this;
        }

        public MemberRefreshTokenBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public MemberRefreshTokenBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public MemberRefreshToken build() {
            return new MemberRefreshToken(this);
        }
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
