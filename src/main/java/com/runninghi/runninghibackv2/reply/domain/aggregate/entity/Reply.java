package com.runninghi.runninghibackv2.reply.domain.aggregate.entity;

import com.runninghi.runninghibackv2.common.entity.BaseTimeEntity;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_REPLY")
@ToString
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO")
    @Comment("작성자")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_NO")
    @Comment("게시글")
    private Post post;

    @Column(nullable = false, length = 1000)
    @Comment("댓글 내용")
    private String replyContent;

    @Column
    @Comment("신고된 횟수")
    private int reportedCount;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    @Comment("삭제 여부")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_NO")
    @Comment("부모 댓글")
    private Reply parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)   // cascade 설정! 부모 댓글 삭제 시 자식 댓글 삭제
    @Comment("하위 댓글들")
    private final List<Reply> children = new ArrayList<>();

    private Reply(ReplyBuilder builder) {
        this.replyNo = builder.replyNo;
        this.writer = builder.writer;
        this.post = builder.post;
        this.replyContent = builder.replyContent;
        this.reportedCount = builder.reportedCount;
        this.isDeleted = builder.isDeleted;
        this.parent = builder.parent;
    }

    public void addChildrenReply (Reply reply) {
        this.children.add(reply);
    }

    public void addParentReply (Reply reply) {
        this.parent = reply;
    }

    public void update (String replyContent) {
        this.replyContent = replyContent;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public static ReplyBuilder builder() {
        return new ReplyBuilder();
    }



    public static class ReplyBuilder {

        private Long replyNo;
        private Member writer;
        private Post post;
        private String replyContent;
        private int reportedCount;
        private boolean isDeleted;
        private Reply parent;

        public ReplyBuilder commentNo(Long replyNo) {
            this.replyNo = replyNo;
            return this;
        }

        public ReplyBuilder writer(Member writer) {
            this.writer = writer;
            return this;
        }

        public ReplyBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public ReplyBuilder replyContent(String replyContent) {
            this.replyContent = replyContent;
            return this;
        }

        public ReplyBuilder reportedCount(int reportedCount) {
            this.reportedCount = reportedCount;
            return this;
        }

        public ReplyBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public ReplyBuilder parent(Reply parent) {
            this.parent = parent;
            return this;
        }

        public Reply build() {
            return new Reply(this);
        }


    }

}
