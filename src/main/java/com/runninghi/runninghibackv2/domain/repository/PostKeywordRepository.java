package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.PostKeyword;
import com.runninghi.runninghibackv2.domain.entity.vo.PostKeywordId;
import com.runninghi.runninghibackv2.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostKeywordRepository extends JpaRepository<PostKeyword, PostKeywordId> {

    List<PostKeyword> findAllByPost(Post post);
    Optional<List<PostKeyword>> deleteAllByPostKeywordId_PostNo(@Param(("postNo")) Long postNo);
}
