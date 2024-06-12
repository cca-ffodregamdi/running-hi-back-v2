package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.entity.vo.RunDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column
    @Comment("닉네임")
    private String nickname;

    @Column
    @Comment("프로필 이미지 url")
    private String profileUrl;

    @Column
    @Comment("카카오 로그인 : id")
    private String kakaoId;

    @Column
    @Comment("카카오 로그인 : 카카오에 설정된 이름")
    private String name;

    @Column
    @Comment("애플 로그인 : id")
    private String appleId;

    @Column
    @Comment("신고된 횟수")
    private int reportCnt;

    @Column
    @Comment("계정 활성화 상태")
    private boolean isActive = true;

    @Column
    @Comment("블랙리스트 상태")
    private boolean isBlacklisted = false;

    @Column
    @Enumerated(EnumType.STRING)
    @Comment("권한")
    private Role role;

    @Column(columnDefinition = "TEXT")
    @Comment("리프레시 토큰")
    private String refreshToken;

    @Column
    @Comment("애플 리프레시 토큰")
    private String appleRefreshToken;

    @Column
    @Comment("FCM 기기 고유 토큰")
    private String fcmToken;

    @Column
    @ColumnDefault(value = "false")
    @Comment("알림 수신 동의 여부")
    private boolean alarmConsent;

    @Column
    @Comment("탈퇴 신청 날짜")
    private LocalDateTime deactivateDate;

   @Embedded
   private RunDataVO runDataVO;

    @Builder
    public Member(Long memberNo, String account, String password, String nickname, String profileUrl, String kakaoId, String name,
                  String appleId, int reportCnt, boolean isActive, boolean isBlacklisted, Role role, String refreshToken,
                  String appleRefreshToken, String fcmToken, boolean alarmConsent, LocalDateTime deactivateDate,
                  RunDataVO runDataVO) {
        this.memberNo = memberNo;
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.kakaoId = kakaoId;
        this.name = name;
        this.appleId = appleId;
        this.reportCnt = reportCnt;
        this.isActive = isActive;
        this.isBlacklisted = isBlacklisted;
        this.role = role;
        this.refreshToken = refreshToken;
        this.appleRefreshToken = appleRefreshToken;
        this.fcmToken = fcmToken;
        this.alarmConsent = alarmConsent;
        this.deactivateDate = deactivateDate;
        this.runDataVO = runDataVO;
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
        this.name = null;
        this.reportCnt = 0;
        this.isActive = false;
        this.isBlacklisted = false;
        this.refreshToken = null;
        this.fcmToken = null;
        this.alarmConsent = false;
        this.runDataVO.cleanupDeactivateMemberRunData();
    }

}