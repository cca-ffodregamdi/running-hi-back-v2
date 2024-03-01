package com.runninghi.runninghibackv2.bookmark.domain.aggregate.vo;


import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkId implements Serializable {

    private Long memberNo;
    private Long bookmarkFolderNo;

    private BookmarkId(BookmarkIdBuilder builder) {
        this.memberNo = builder.memberNo;
        this.bookmarkFolderNo = builder.bookmarkFolderNo;
    }

    public static class BookmarkIdBuilder {
        private Long memberNo;
        private Long bookmarkFolderNo;

        public static BookmarkIdBuilder builder() {
            return new BookmarkIdBuilder();
        }

        public BookmarkIdBuilder memberNo(Long memberNo) {
            this.memberNo = memberNo;
            return this;
        }

        public BookmarkIdBuilder bookmarkFolderNo(Long bookmarkFolderNo) {
            this.bookmarkFolderNo = bookmarkFolderNo;
            return this;
        }

        public BookmarkId build() {
            return new BookmarkId(this);
        }
    }
}
