package com.runninghi.runninghibackv2.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "tbl_notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_no")
    private Long noticeNo;

    @Column(name = "title", length = 100, nullable = false)
    @Comment("공지사항 제목")
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    @Comment("공지사항 내용 (Base64 인코딩된 이미지 포함 가능)")
    private String content;

    @Builder
    public Notice(Long noticeNo, String title, String content) {
        this.noticeNo = noticeNo;
        this.title = title;
        this.content = content;
    }
}
