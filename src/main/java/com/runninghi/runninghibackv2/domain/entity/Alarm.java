package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.application.dto.alarm.response.GetAllAlarmResponse;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tbl_alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_no")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_NO")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Comment("알림 대상자")
    private Member member;

    @Column(name = "title", length = 100)
    @Comment(value = "알림 제목")
    private String title;

    @Column(name = "content", nullable = false, length = 500)
    @Comment(value = "알림 내용")
    private String content;

    @Column(name = "is_read", nullable = false)
    @ColumnDefault(value = "false")
    @Comment(value = "확인 여부")
    private boolean isRead;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    @Comment(value = "알림 생성 시간")
    private LocalDateTime createDate;

    @Column(name = "read_date", updatable = false)
    @Comment(value = "알림 확인 시간")
    private LocalDateTime readDate;

    @Builder
    public Alarm(Long id, Member member, String title, String content, boolean isRead, LocalDateTime createDate, LocalDateTime readDate) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.isRead = false;
        this.createDate = createDate;
        this.readDate = readDate;
    }

    public static GetAllAlarmResponse toResponse(Alarm alarm) {
        return new GetAllAlarmResponse(
                alarm.getTitle(),
                alarm.getContent(),
                alarm.isRead(),
                alarm.createDate
        );
    }

}
