package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "TBL_POST_REPORT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postReportNo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("신고 사유 카테고리")
    private ReportCategory category;

    @Column
    @Comment("기타 신고 사유")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("신고 처리 상태")
    private ProcessingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_no")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Comment("신고자")
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_post_no")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Comment("신고된 게시글")
    private Post reportedPost;

    @Column(nullable = false)
    @Comment("신고된 게시글 내용")
    private String postContent;

    @Builder
    public PostReport(Long postReportNo, ReportCategory category, String content, ProcessingStatus status,
                      Member reporter, Post reportedPost, String postContent) {
        this.postReportNo = postReportNo;
        this.category = category;
        this.content = content;
        this.status = status;
        this.reporter = reporter;
        this.reportedPost = reportedPost;
        this.postContent = postContent;
    }

    public void update(ProcessingStatus status) {
        this.status = status;
    }
}
