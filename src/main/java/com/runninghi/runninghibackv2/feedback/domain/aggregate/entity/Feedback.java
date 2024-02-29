package com.runninghi.runninghibackv2.feedback.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "TBL_FEEDBACK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @JoinColumn(name = "feedback_writer_no", nullable = false)
    @Comment("피드백 작성자")
    private Member feedbackWriter;


    public Feedback(Builder builder) {
        this.feedbackNo = builder.feedbackNo;
        this.title = builder.title;
        this.content = builder.content;
        this.hasReply = builder.hasReply;
        this.reply = builder.reply;
        this.category = builder.category;
        this.feedbackWriter = builder.feedbackWriter;
    }

    public static class Builder {
        private Long feedbackNo;
        private String title;
        private String content;
        private boolean hasReply;
        private String reply;
        private FeedbackCategory category;
        private Member feedbackWriter;

        public Builder feedbackNo(Long feedbackNo) {
            this.feedbackNo = feedbackNo;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder hasReply(boolean hasReply) {
            this.hasReply = hasReply;
            return this;
        }

        public Builder reply(String reply) {
            this.reply = reply;
            return this;
        }

        public Builder category(FeedbackCategory category) {
            this.category = category;
            return this;
        }

        public Builder feedbackWriter(Member feedbackWriter) {
            this.feedbackWriter = feedbackWriter;
            return this;
        }

        public Feedback builder() {
            return new Feedback(this);
        }
    }

}
