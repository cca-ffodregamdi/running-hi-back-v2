package com.runninghi.runninghibackv2.comment.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
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
    @JoinColumn(name = "MEMBER_NO")
    @org.hibernate.annotations.Comment("작성자")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_NO")
    @org.hibernate.annotations.Comment("게시글")
    private Post post;

    @Column(nullable = false, length = 1000)
    @org.hibernate.annotations.Comment("댓글 내용")
    private String commentContent;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    @org.hibernate.annotations.Comment("삭제 여부")
    private Boolean isDeleted;

//    @Column
//    private Long parent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_NO")
    @org.hibernate.annotations.Comment("부모 댓글")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @org.hibernate.annotations.Comment("하위 댓글들")
    private List<Comment> children = new ArrayList<>();

    private Comment(CommentBuilder builder) {
        this.commentNo = builder.commentNo;
        this.writer = builder.writer;
        this.post = builder.post;
        this.commentContent = builder.commentContent;
        this.isDeleted = builder.isDeleted;
        this.parent = builder.parent;
    }

    public static class CommentBuilder {

        private Long commentNo;
        private Member writer;
        private Post post;
        private String commentContent;
        private boolean isDeleted;
//        private Comment parent;
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

        public CommentBuilder memberPost(Post memberPost) {
            this.post = memberPost;
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
