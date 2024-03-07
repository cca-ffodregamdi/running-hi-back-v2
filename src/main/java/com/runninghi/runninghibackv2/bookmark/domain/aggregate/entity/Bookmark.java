package com.runninghi.runninghibackv2.bookmark.domain.aggregate.entity;

import com.runninghi.runninghibackv2.bookmark.domain.aggregate.vo.BookmarkId;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_BOOKMARK")
public class Bookmark {

    @EmbeddedId
    private BookmarkId bookmarkId;

    @MapsId(value = "memberNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO")
    private Member member;

//    @MapsId(value = "bookmarkFolderNo")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "BOOKMARK_FOLDER_NO")
//    private BookmarkFolder bookmarkFolder;

    @MapsId(value = "postNo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_NO")
    private Post post;

    private Bookmark (BookmarkBuilder builder) {
        this.bookmarkId = builder.bookmarkId;
        this.member = builder.member;
        this.post = builder.post;
    }

    public static class BookmarkBuilder {
        private BookmarkId bookmarkId;
        private Member member;
        private Post post;

        public static BookmarkBuilder builder() {
            return new BookmarkBuilder();
        }

        public BookmarkBuilder bookmarkId(BookmarkId bookmarkId) {
            this.bookmarkId = bookmarkId;
            return this;
        }

        public BookmarkBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public BookmarkBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public Bookmark build() {
            return new Bookmark(this);
        }

    }


}
