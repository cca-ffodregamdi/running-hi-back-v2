package com.runninghi.runninghibackv2.domain.entity.vo;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LikeId implements Serializable {

    @Comment("멤버 번호")
    private Long memberNo;

    @Comment("게시글 번호")
    private Long postNo;

    public static LikeId of (Long memberNo, Long postNo) {
        return new LikeId(memberNo, postNo);
    }
}
