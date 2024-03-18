package com.runninghi.runninghibackv2.post.postkeyword.domain.repository;

import com.runninghi.runninghibackv2.bookmark.domain.aggregate.entity.Bookmark;
import com.runninghi.runninghibackv2.post.postkeyword.domain.aggregate.entity.PostKeyword;
import com.runninghi.runninghibackv2.post.postkeyword.domain.aggregate.vo.PostKeywordId;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostKeywordRepository extends JpaRepository<PostKeyword, PostKeywordId> {

    List<PostKeyword> findPostKeywordsByPostKeywordVO_Post(Post post);
    Optional<List<PostKeyword>> deleteAllByPostKeywordId_Post(@Param(("postNo")) Long postNo);
}
