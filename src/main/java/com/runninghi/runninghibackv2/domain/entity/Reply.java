package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_REPLY")
@ToString
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Comment("작성자")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "POST_NO")
    @Comment("게시글")
    private Post post;

    @Column(nullable = false, length = 1000)
    @Comment("댓글 내용")
    private String replyContent;

    @Column
    @Comment("신고된 횟수")
    private int reportedCount;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    @Comment("삭제 여부")
    private boolean isDeleted;

    @OneToMany(mappedBy = "reportedReply")
    @Comment("댓글 신고 리스트")
    private final List<ReplyReport> reportList = new ArrayList<>();

    private Reply(ReplyBuilder builder) {
        this.replyNo = builder.replyNo;
        this.writer = builder.writer;
        this.post = builder.post;
        this.replyContent = builder.replyContent;
        this.reportedCount = builder.reportedCount;
        this.isDeleted = builder.isDeleted;
    }

    public void addReportedCount () {this.reportedCount++;}

    public void resetReportedCount() {
        this.reportedCount = 0;
    }

    public void update (String replyContent) {
        this.replyContent = replyContent;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void addReplyReport(ReplyReport replyReport) { this.reportList.add(replyReport);}


    @Builder
    public Reply(Long replyNo, Member writer, Post post, String replyContent, int reportedCount, boolean isDeleted) {
        this.replyNo = replyNo;
        this.writer = writer;
        this.post = post;
        this.replyContent = replyContent;
        this.reportedCount = reportedCount;
        this.isDeleted = isDeleted;
    }

}
