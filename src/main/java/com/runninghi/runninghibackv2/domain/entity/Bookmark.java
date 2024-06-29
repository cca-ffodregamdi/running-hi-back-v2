package com.runninghi.runninghibackv2.domain.entity;

import com.runninghi.runninghibackv2.domain.entity.vo.BookmarkId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_BOOKMARK")
public class Bookmark {

    @EmbeddedId
    private BookmarkId bookmarkId;

    @MapsId(value = "memberNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MEMBER_NO")
    private Member member;


    @MapsId(value = "postNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "POST_NO")
    private Post post;

    @Builder
    public Bookmark(BookmarkId bookmarkId, Member member, Post post) {
        this.bookmarkId = bookmarkId;
        this.member = member;
        this.post = post;
    }

}
