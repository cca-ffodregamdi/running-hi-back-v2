package com.runninghi.runninghibackv2.keyword.postkeyword.domain.aggregate.entity;

import com.runninghi.runninghibackv2.keyword.postkeyword.domain.aggregate.vo.PostKeywordVO;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Table(name = "TBL_POST_KEYWORD")
public class PostKeyword implements Serializable {

    @EmbeddedId
    private PostKeywordVO postKeywordVO;

    public PostKeyword(PostKeywordVO postKeywordVO) {
        this.postKeywordVO = postKeywordVO;
    }

}
