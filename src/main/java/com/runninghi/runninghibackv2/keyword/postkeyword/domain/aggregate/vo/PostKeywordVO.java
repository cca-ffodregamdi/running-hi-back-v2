package com.runninghi.runninghibackv2.keyword.postkeyword.domain.aggregate.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

    @Column
    private Long keywordNo;

    @Column
    private Long postNo;

    private PostKeywordVO(Builder builder) {
        this.keywordNo = builder.keywordNo;
        this.postNo = builder.postNo;
    }

    public static class Builder {
        private Long keywordNo;
        private Long postNo;

        public Builder keywordNo(Long keywordNo) {
            this.keywordNo = keywordNo;
            return this;
        }

        public Builder postNo(Long postNo) {
            this.postNo = postNo;
            return this;
        }

        public PostKeywordVO build() {
            return new PostKeywordVO(this);
        }
    }

}
