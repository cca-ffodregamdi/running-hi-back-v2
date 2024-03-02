package com.runninghi.runninghibackv2.like.domain.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeId implements Serializable {

    @Comment("멤버 번호")
    private Long memberNo;
    @Comment("게시물 번호")
    private Long postNo;


    private LikeId(LikeIdBuilder builder) {
        this.memberNo = builder.memberNo;
        this.postNo = builder.postNo;
    }

    public static class LikeIdBuilder {
        private Long memberNo;
        private Long postNo;

        public static LikeIdBuilder builder() {
            return new LikeIdBuilder();
        }

        public LikeIdBuilder memberNo(Long memberNo) {
            this.memberNo = memberNo;
            return this;
        }

        public LikeIdBuilder memberPostNo(Long postNo) {
            this.postNo = postNo;
            return this;
        }

        public LikeId build() {
            return new LikeId(this);
        }
    }

}
