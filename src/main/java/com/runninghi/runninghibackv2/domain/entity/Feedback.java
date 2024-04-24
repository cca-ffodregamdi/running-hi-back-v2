package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.enumtype.FeedbackCategory;
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Comment("피드백 작성자")
    private Member feedbackWriter;

    @Builder
    public Feedback(Long feedbackNo, String title, String content, boolean hasReply, String reply,
                    FeedbackCategory category, Member feedbackWriter) {
        this.feedbackNo = feedbackNo;
        this.title = title;
        this.content = content;
        this.hasReply = hasReply;
        this.reply = reply;
        this.category = category;
        this.feedbackWriter = feedbackWriter;
    }

}