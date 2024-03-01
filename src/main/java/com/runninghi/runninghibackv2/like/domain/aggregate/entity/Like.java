package com.runninghi.runninghibackv2.like.domain.aggregate.entity;

import com.runninghi.runninghibackv2.like.domain.aggregate.vo.LikeId;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_LIKE")
public class Like {

    @EmbeddedId
    private LikeId likeId;

    @MapsId(value = "memberNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member writer;

    @MapsId(value = "postNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    private Post post;

    private Like(LikeBuilder builder){
        this.likeId = builder.likeId;
        this.writer = builder.writer;
        this.post = builder.post;
    }

    public static class LikeBuilder {

        private LikeId likeId;
        private Member writer;
        private Post post;

        public static LikeBuilder builder() {return new LikeBuilder();}

        public LikeBuilder likeId(LikeId likeId) {
            this.likeId = likeId;
            return this;
        }

        public LikeBuilder writer(Member writer) {
            this.writer = writer;
            return this;
        }

        public LikeBuilder memberPost(Post post) {
            this.post = post;
            return this;
        }

        public Like build() {
            return new Like(this);
        }
    }
}
