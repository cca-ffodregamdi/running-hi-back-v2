package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
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

    @Column
    @Comment("리프레시 토큰")
    private String refreshToken;

    @Column
    @Comment("FCM 기기 고유 토큰")
    private String fcmToken;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    @Comment("알림 수신 동의 여부")
    private boolean alarmConsent;

    @Column
    @Comment("탈퇴 신청 날짜")
    private LocalDateTime deactivateDate;

    @Column(nullable = false)
    @Comment("누적 거리")
    private double totalDistance = 0.0;

    @Column(nullable = false)
    @Comment("누적 칼로리")
    private double totalKcal = 0.0;

    @Column(nullable = false)
    @Comment("다음 레벨에 필요한 거리")
    private int distanceToNextLevel = 10;

    @Column
    @Comment("누적거리에 따른 레벨")
    private int level = 0;

    @Builder
    public Member(Long memberNo, String account, String password, String nickname, String kakaoId, String kakaoName,
                  int reportCnt, boolean isActive, boolean isBlacklisted, Role role, String refreshToken,
                  String fcmToken, boolean alarmConsent, LocalDateTime deactivateDate, double totalDistance,
                  double totalKcal, int distanceToNextLevel, int level) {
        this.memberNo = memberNo;
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.kakaoName = kakaoName;
        this.reportCnt = reportCnt;
        this.isActive = isActive;
        this.isBlacklisted = isBlacklisted;
        this.role = role;
        this.refreshToken = refreshToken;
        this.fcmToken = fcmToken;
        this.alarmConsent = alarmConsent;
        this.deactivateDate = deactivateDate;
        this.totalDistance = totalDistance;
        this.totalKcal = totalKcal;
        this.distanceToNextLevel = distanceToNextLevel;
        this.level = level;
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

    public void updateFCMToken(String fcmToken) {this.fcmToken = fcmToken; }

    public void updateAlarmConsent(boolean alarmConsent) {this.alarmConsent = alarmConsent;}

    public void cleanupDeactivateMemberData() {
        this.account = null;
        this.password = null;
        this.nickname = null;
        this.kakaoId = null;
        this.kakaoName = null;
        this.reportCnt = 0;
        this.isActive = false;
        this.isBlacklisted = false;
        this.refreshToken = null;
        this.fcmToken = null;
        this.alarmConsent = false;
        this.totalDistance = 0;
        this.totalKcal = 0;
        this.distanceToNextLevel = 0;
        this.level = 0;
    }

}