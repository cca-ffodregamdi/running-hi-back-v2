package com.runninghi.runninghibackv2.like.domain.aggregate.entity;

import com.runninghi.runninghibackv2.like.domain.aggregate.vo.LikeId;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.memberpost.domain.aggregate.entity.MemberPost;
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

    @MapsId(value = "memberPostNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_post_no")
    private MemberPost memberPost;

    private Like(LikeBuilder builder){
        this.likeId = builder.likeId;
        this.writer = builder.writer;
        this.memberPost = builder.memberPost;
    }

    public static class LikeBuilder {

        private LikeId likeId;
        private Member writer;
        private MemberPost memberPost;

        public static LikeBuilder builder() {return new LikeBuilder();}

        public LikeBuilder likeId(LikeId likeId) {
            this.likeId = likeId;
            return this;
        }

        public LikeBuilder writer(Member writer) {
            this.writer = writer;
            return this;
        }

        public LikeBuilder memberPost(MemberPost memberPost) {
            this.memberPost = memberPost;
            return this;
        }

        public Like build() {
            return new Like(this);
        }
    }
}
