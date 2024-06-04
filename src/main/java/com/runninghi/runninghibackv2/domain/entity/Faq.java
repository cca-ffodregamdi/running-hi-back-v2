package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.application.dto.faq.request.UpdateFaqRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "TBL_FAQ")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_no")
    private Long faqNo;

    @Column(name = "question", length = 100, nullable = false)
    @Comment("faq 질문")
    private String question;

    @Column(name = "answer", nullable = false)
    @Comment("faq 답변")
    private String answer;

    @Builder
    public Faq(Long faqNo, String question, String answer) {
        this.faqNo = faqNo;
        this.question = question;
        this.answer = answer;
    }

    public void update(UpdateFaqRequest request) {
        this.question = request.question();
        this.answer = request.answer();
    }


}
