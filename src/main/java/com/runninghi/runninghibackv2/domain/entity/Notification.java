package com.runninghi.runninghibackv2.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TBL_NOTIFICATION")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_no")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_NO")
    @Comment("회원 번호")
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
    public Notification(Long id, Member member, String title, String content, boolean isRead, LocalDateTime createDate, LocalDateTime readDate) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
        this.createDate = createDate;
        this.readDate = readDate;
    }

}
