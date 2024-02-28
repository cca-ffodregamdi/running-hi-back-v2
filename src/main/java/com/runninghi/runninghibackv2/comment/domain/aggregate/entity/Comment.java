package com.runninghi.runninghibackv2.comment.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Table(name = "TBL_COMMENT")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBER_KEY")
//    private Member memberKey;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "USERPOST_NO")
//    private UserPost userPostNo;

    @Column(nullable = false)
    private String commentContent;

    @Column(nullable = false)
    private boolean commentStatus;

    @Column
    private Long parent;



}
