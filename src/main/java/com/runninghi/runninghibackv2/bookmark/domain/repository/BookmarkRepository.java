package com.runninghi.runninghibackv2.bookmark.domain.repository;


import com.runninghi.runninghibackv2.bookmark.domain.aggregate.entity.Bookmark;
import com.runninghi.runninghibackv2.bookmark.domain.aggregate.vo.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {

    List<Bookmark> findAllByBookmarkId_MemberNo (@Param("memberNo") Long memberNo);
}
