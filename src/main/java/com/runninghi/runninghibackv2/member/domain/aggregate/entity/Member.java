package com.runninghi.runninghibackv2.member.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.common.entity.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

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

    @Column
    @Comment("탈퇴 신청 날짜")
    private LocalDateTime deactivateDate;

    @Column
    @Comment("누적 거리")
    private double totalDistance = 0;

    @Column
    @Comment("누적 칼로리")
    private Long totalKcal = 0L;

    @Column
    @Comment("다음 레벨에 필요한 거리")
    private int distanceToNextLevel = 10;

    @Column
    @Comment("누적거리에 따른 레벨")
    private int level;


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
        this.deactivateDate = memberBuilder.deactivateDate;
        this.totalDistance = memberBuilder.totalDistance;
        this.totalKcal = memberBuilder.totalKcal;
        this.distanceToNextLevel = memberBuilder.distanceToNextLevel;
        this.level = memberBuilder.level;
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
        private LocalDateTime deactivateDate;
        private double totalDistance;
        private Long totalKcal;
        private int distanceToNextLevel;
        private int level;

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

        public MemberBuilder deactivateDate(LocalDateTime deactivateDate) {
            this.deactivateDate = deactivateDate;
            return this;
        }

        public MemberBuilder totalDistance(double totalDistance) {
            this.totalDistance = totalDistance;
            return this;
        }

        public MemberBuilder totalKcal(Long totalKcal) {
            this.totalKcal = totalKcal;
            return this;
        }

        public MemberBuilder distanceToNextLevel(int distanceToNextLevel) {
            this.distanceToNextLevel = distanceToNextLevel;
            return this;
        }

        public MemberBuilder level(int level) {
            this.level = level;
            return this;
        }

        public Member build() {
            return new Member(this);
        }

    }

    // 리프레시 토큰 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 닉네임 업데이트
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 멤버 비활성화 (탈퇴 처리)
    public void deactivateMember() {
        this.isActive = false;
        this.deactivateDate = LocalDateTime.now();
    }

    // 멤버 활성화
    public void activateMember() {
        this.isActive = true;
        this.deactivateDate = null;
    }

    // 누적 거리 업데이트
    public void updateTotalDistanceAndLevel(double distance) {
        this.totalDistance += distance;
        checkAndLevelUp();
    }

    // 레벨 업데이트를 확인하고 처리
    private void checkAndLevelUp() {
        while (this.totalDistance >= this.distanceToNextLevel) {
            this.level++;
            this.distanceToNextLevel = calculateDistanceForNextLevel();
        }
    }

    // 다음 레벨까지의 거리 업데이트
    // 이전 레벨업에 필요했던 거리의 1.5배로 계산하되, 10 단위로 떨어지지않을 경우 올림 처리
    private int calculateDistanceForNextLevel() {
        int result = (int)(distanceToNextLevel * 1.5);

        if (result % 10 != 0) {
            result = result + (10 - result % 10);
        }

        return result;
    }

    public void addReportedCount() {
        this.reportCnt += 1;
    }

}
