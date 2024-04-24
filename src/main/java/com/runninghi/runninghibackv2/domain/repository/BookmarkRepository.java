package com.runninghi.runninghibackv2.domain.repository;


import com.runninghi.runninghibackv2.domain.entity.Bookmark;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.vo.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {

    List<Bookmark> findAllByBookmarkId_MemberNo (@Param("memberNo") Long memberNo);

    void deleteAllByMember(Member member);

    void deleteAllByPost(Post post);

}
