package com.runninghi.runninghibackv2.post.postkeyword.domain.aggregate.vo;

import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode
public class PostKeywordVO implements Serializable {

    @ManyToOne
    @JoinColumn(name = "KEYWORD_NO")
    private Keyword keyword;

    @ManyToOne
    @JoinColumn(name = "POST_NO")
    private Post post;

    private PostKeywordVO(PostKeywordBuilder builder) {
        this.keyword = builder.keyword;
        this.post = builder.post;
    }
    public static PostKeywordBuilder builder() {
        return new PostKeywordBuilder();
    }

    public static class PostKeywordBuilder {
        private Keyword keyword;
        private Post post;

        public PostKeywordBuilder keyword(Keyword keyword) {
            this.keyword = keyword;
            return this;
        }

        public PostKeywordBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public PostKeywordVO build() {
            return new PostKeywordVO(this);
        }
    }

}
