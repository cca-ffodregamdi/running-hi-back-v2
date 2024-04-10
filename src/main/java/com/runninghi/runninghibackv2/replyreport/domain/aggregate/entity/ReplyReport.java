package com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.common.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "TBL_REPLY_REPORT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyReportNo;

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
    @Comment("신고자")
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_reply_no")
    @Comment("신고된 댓글")
    private Reply reportedReply;

    @Column(nullable = false)
    @Comment("신고된 댓글 내용")
    private String replyContent;

    @Column(nullable = false)
    @Comment("연관된 댓글이 삭제되었는지 여부")
    private boolean isReplyDeleted;

    public ReplyReport(Builder builder) {
        this.replyReportNo = builder.replyReportNo;
        this.category = builder.category;
        this.content = builder.content;
        this.status = builder.status;
        this.reporter = builder.reporter;
        this.reportedReply = builder.reportedReply;
        this.replyContent = builder.replyContent;
        this.isReplyDeleted = builder.isReplyDeleted;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long replyReportNo;
        private ReportCategory category;
        private String content;
        private ProcessingStatus status;
        private Member reporter;
        private Reply reportedReply;
        private String  replyContent;
        private boolean isReplyDeleted;

        public Builder replyReportNo(Long replyReportNo) {
            this.replyReportNo = replyReportNo;
            return this;
        }

        public Builder category(ReportCategory category) {
            this.category = category;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder status(ProcessingStatus status) {
            this.status = status;
            return this;
        }
        public Builder reporter(Member reporter) {
            this.reporter = reporter;
            return this;
        }

        public Builder reportedReply(Reply reportedReply) {
            this.reportedReply = reportedReply;
            return this;
        }

        public Builder replyContent(String replyContent) {
            this.replyContent = replyContent;
            return this;
        }

        public Builder isReplyDeleted(boolean isReplyDeleted) {
            this.isReplyDeleted = isReplyDeleted;
            return this;
        }

        public ReplyReport build() {
            return new ReplyReport(this);
        }
    }

    public void update(ProcessingStatus status, boolean isReplyDeleted, Reply reportedReply) {
        this.status = status;
        this.isReplyDeleted = isReplyDeleted;
        this.reportedReply = reportedReply;
    }

    public void update(ProcessingStatus status) {
        this.status = status;
    }
}
