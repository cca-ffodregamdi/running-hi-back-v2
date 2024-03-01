package com.runninghi.runninghibackv2.comment.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.memberpost.domain.aggregate.entity.MemberPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_COMMENT")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_No")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_POST_NO")
    private MemberPost memberPost;

    @Column(nullable = false, length = 1000)
    private String commentContent;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_NO")
    private Comment parent;

    private Comment(CommentBuilder builder) {
        this.commentNo = builder.commentNo;
        this.writer = builder.writer;
        this.memberPost = builder.memberPost;
        this.commentContent = builder.commentContent;
        this.isDeleted = builder.isDeleted;
        this.parent = builder.parent;
    }

    public static class CommentBuilder {

        private Long commentNo;
        private Member writer;
        private MemberPost memberPost;
        private String commentContent;
        private boolean isDeleted;
        private Comment parent;

        public static CommentBuilder builder() {
            return new CommentBuilder();
        }

        public CommentBuilder commentNo(Long commentNo) {
            this.commentNo = commentNo;
            return this;
        }

        public CommentBuilder writer(Member writer) {
            this.writer = writer;
            return this;
        }

        public CommentBuilder memberPost(MemberPost memberPost) {
            this.memberPost = memberPost;
            return this;
        }

        public CommentBuilder commentContent(String commentContent) {
            this.commentContent = commentContent;
            return this;
        }

        public CommentBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public CommentBuilder parent(Comment parent) {
            this.parent = parent;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }


    }
}
