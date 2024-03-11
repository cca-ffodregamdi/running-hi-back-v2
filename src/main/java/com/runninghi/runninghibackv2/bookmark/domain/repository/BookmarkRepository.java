package com.runninghi.runninghibackv2.bookmark.domain.repository;


import com.runninghi.runninghibackv2.bookmark.domain.aggregate.entity.Bookmark;
import com.runninghi.runninghibackv2.bookmark.domain.aggregate.vo.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {

    @Query(value = "SELECT *" +
                    " FROM tbl_bookmark" +
                    "WHERE MEMBER_NO = :memberNo", nativeQuery = true)
    Optional<List<Bookmark>> findAllByMemberNo (@Param("memberNo") Long memberNo);
}
