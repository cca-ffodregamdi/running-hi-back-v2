package com.runninghi.runninghibackv2.bookmarkfolder.domain.aggregate.entity;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_BOOKMARK_FOLDER")
public class BookmarkFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkFolderNo;

    @Column(nullable = false, length = 30)
    @Comment("폴더 이름")
    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO")
    @Comment("멤버 번호")
    private Member member;

    private BookmarkFolder(BookmarkFolderBuilder builder) {
        this.bookmarkFolderNo = builder.bookmarkFolderNo;
        this.folderName = builder.folderName;
        this.member = builder.member;
    }

    public static class BookmarkFolderBuilder {

        private Long bookmarkFolderNo;
        private String folderName;
        private Member member;

        public static BookmarkFolderBuilder builder() {
            return new BookmarkFolderBuilder();
        }

        public BookmarkFolderBuilder bookmarkFolderNo(Long bookmarkFolderNo) {
            this.bookmarkFolderNo = bookmarkFolderNo;
            return this;
        }

        public BookmarkFolderBuilder folderName(String folderName) {
            this.folderName = folderName;
            return this;
        }

        public BookmarkFolderBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public BookmarkFolder build() {
            return new BookmarkFolder(this);
        }

    }

}
