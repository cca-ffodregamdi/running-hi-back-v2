package com.runninghi.runninghibackv2.domain.entity.vo;


import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class   BookmarkId {

    @Comment("멤버 번호")
    private Long memberNo;

    @Comment("게시물 번호")
    private Long postNo;

    public static BookmarkId of (Long memberNo, Long postNo) {
        return new BookmarkId(memberNo, postNo);
    }
}
