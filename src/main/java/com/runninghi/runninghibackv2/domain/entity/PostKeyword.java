package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.entity.vo.PostKeywordId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_POST_KEYWORD")
public class PostKeyword implements Serializable {

    @EmbeddedId
    private PostKeywordId postKeywordId;

    @MapsId(value = "keywordNo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "KEYWORD_NO")
    private Keyword keyword;

    @MapsId(value = "postNo")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "POST_NO")
    private Post post;

    @Builder
    public PostKeyword(PostKeywordId postKeywordId, Keyword keyword, Post post) {
        this.postKeywordId = postKeywordId;
        this.keyword = keyword;
        this.post = post;
    }
}