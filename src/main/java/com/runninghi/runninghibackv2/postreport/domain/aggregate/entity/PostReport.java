package com.runninghi.runninghibackv2.postreport.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.common.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "TBL_POST_REPORT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postReportNo;

    @Column(nullable = false)
    @Comment("신고 사유 카테고리")
    private ReportCategory category;

    @Column
    @Comment("기타 신고 사유")
    private String content;

    @Column(nullable = false)
    @Comment("연관된 게시글이 삭제되었는지 여부")
    private boolean reportedPostDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_no", nullable = false)
    @Comment("신고자")
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_no", nullable = false)
    @Comment("피신고자")
    private Member reportedMember;

    // reportedPost 연관관계

    public PostReport(Builder builder) {
        this.postReportNo = builder.postReportNo;
        this.category = builder.category;
        this.content = builder.content;
        this.reportedPostDeleted = builder.reportedPostDeleted;
        this.reporter = builder.reporter;
        this.reportedMember = builder.reportedMember;
    }

    public static class Builder {
        private Long postReportNo;
        private ReportCategory category;
        private String content;
        private boolean reportedPostDeleted;
        private Member reporter;
        private Member reportedMember;

        public Builder reportNo(Long postReportNo) {
            this.postReportNo = postReportNo;
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

        public Builder reportedPostDeleted(boolean reportedPostDeleted) {
            this.reportedPostDeleted = reportedPostDeleted;
            return this;
        }

        public Builder reporter(Member reporter) {
            this.reporter = reporter;
            return this;
        }

        public Builder reportedMember(Member reportedMember) {
            this.reportedMember = reportedMember;
            return this;
        }

        public PostReport build() {
            return new PostReport(this);
        }

    }

}
