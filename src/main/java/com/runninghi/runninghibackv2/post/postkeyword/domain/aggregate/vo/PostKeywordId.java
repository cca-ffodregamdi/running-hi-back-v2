package com.runninghi.runninghibackv2.post.postkeyword.domain.aggregate.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode
public class PostKeywordId implements Serializable {

    @Comment("게시글 키워드")
    private Long keywordNo;

    @Comment("게시글")
    private Long postNo;

    private PostKeywordId(PostKeywordIdBuilder builder) {
        this.keywordNo = builder.keywordNo;
        this.postNo = builder.postNo;
    }

    public static PostKeywordIdBuilder builder() {
        return new PostKeywordIdBuilder();
    }

    public static class PostKeywordIdBuilder {
        private Long keywordNo;
        private Long postNo;


        public PostKeywordIdBuilder keywordNo(Long keywordNo) {
            this.keywordNo = keywordNo;
            return this;
        }

        public PostKeywordIdBuilder postNo(Long postNo) {
            this.postNo = postNo;
            return this;
        }

        public PostKeywordId build() {
            return new PostKeywordId(this);
        }
    }

}
