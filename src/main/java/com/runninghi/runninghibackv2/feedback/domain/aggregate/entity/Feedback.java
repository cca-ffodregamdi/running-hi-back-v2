package com.runninghi.runninghibackv2.feedback.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "TBL_FEEDBACK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackNo;

    @Column(nullable = false)
    @Comment("피드백 제목")
    private String title;

    @Column(nullable = false)
    @Comment("피드백 내용")
    private String content;

    @Column(nullable = false)
    @Comment("피드백 답변 진행상황")
    private boolean hasReply;

    @Column
    @Comment("피드백 답변")
    private String reply;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("피드백 카테고리")
    private FeedbackCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    @Comment("피드백 작성자")
    private Member feedbackWriter;


    public Feedback(FeedbackBuilder feedbackBuilder) {
        this.feedbackNo = feedbackBuilder.feedbackNo;
        this.title = feedbackBuilder.title;
        this.content = feedbackBuilder.content;
        this.hasReply = feedbackBuilder.hasReply;
        this.reply = feedbackBuilder.reply;
        this.category = feedbackBuilder.category;
        this.feedbackWriter = feedbackBuilder.feedbackWriter;
    }

    public static FeedbackBuilder builder() {
        return new FeedbackBuilder();
    }

    public static class FeedbackBuilder {
        private Long feedbackNo;
        private String title;
        private String content;
        private boolean hasReply;
        private String reply;
        private FeedbackCategory category;
        private Member feedbackWriter;

        public FeedbackBuilder feedbackNo(Long feedbackNo) {
            this.feedbackNo = feedbackNo;
            return this;
        }

        public FeedbackBuilder title(String title) {
            this.title = title;
            return this;
        }

        public FeedbackBuilder content(String content) {
            this.content = content;
            return this;
        }

        public FeedbackBuilder hasReply(boolean hasReply) {
            this.hasReply = hasReply;
            return this;
        }

        public FeedbackBuilder reply(String reply) {
            this.reply = reply;
            return this;
        }

        public FeedbackBuilder category(FeedbackCategory category) {
            this.category = category;
            return this;
        }

        public FeedbackBuilder feedbackWriter(Member feedbackWriter) {
            this.feedbackWriter = feedbackWriter;
            return this;
        }

        public Feedback build() {
            return new Feedback(this);
        }
    }

}
