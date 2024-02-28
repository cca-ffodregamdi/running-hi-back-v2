package com.runninghi.runninghibackv2.feedback.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "TBL_FEEDBACK")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
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

    @Column
    @Comment("피드백 답변 작성 시간")
    private LocalDateTime replyDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("피드백 카테고리")
    private FeedbackCategory category;

    public Feedback(Builder builder) {
        this.feedbackNo = builder.feedbackNo;
        this.title = builder.title;
        this.content = builder.content;
        this.hasReply = builder.hasReply;
        this.reply = builder.reply;
        this.replyDate = builder.replyDate;
        this.category = builder.category;
    }
    public static class Builder {
        private Long feedbackNo;
        private String title;
        private String content;
        private boolean hasReply;
        private String reply;
        private LocalDateTime replyDate;
        private FeedbackCategory category;

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

        public Builder replyDate(LocalDateTime replyDate) {
            this.replyDate = replyDate;
            return this;
        }

        public Builder category(FeedbackCategory category) {
            this.category = category;
            return this;
        }

        public Feedback builder() {
            return new Feedback(this);
        }
    }

}