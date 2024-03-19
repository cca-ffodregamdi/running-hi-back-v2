package com.runninghi.runninghibackv2.post.postkeyword.domain.aggregate.entity;

import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.post.postkeyword.domain.aggregate.vo.PostKeywordId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_POST_KEYWORD")
public class PostKeyword implements Serializable {

    @EmbeddedId
    private PostKeywordId postKeywordId;

    @MapsId(value = "keywordNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KEYWORD_NO")
    private Keyword keyword;

    @MapsId(value = "postNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_NO")
    private Post post;

    private PostKeyword (PostKeywordBuilder builder) {
        this.postKeywordId = builder.postKeywordId;
        this.keyword = builder.keyword;
        this.post = builder.post;
    }

    public static PostKeywordBuilder builder() {
        return new PostKeywordBuilder();
    }

    public static class PostKeywordBuilder {
        private PostKeywordId postKeywordId;
        private Keyword keyword;
        private Post post;

        public PostKeywordBuilder postKeywordId(PostKeywordId postKeywordId) {
            this.postKeywordId = postKeywordId;
            return this;
        }

        public PostKeywordBuilder keyword(Keyword keyword) {
            this.keyword = keyword;
            return this;
        }

        public PostKeywordBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public PostKeyword build() {
            return new PostKeyword(this);
        }

    }
}