package com.runninghi.runninghibackv2.keyword.postkeyword.domain.repository;

import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.keyword.postkeyword.domain.aggregate.entity.PostKeyword;
import com.runninghi.runninghibackv2.keyword.postkeyword.domain.aggregate.vo.PostKeywordVO;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostKeywordRepository extends JpaRepository<PostKeyword, PostKeywordVO> {

    List<PostKeyword> findPostKeywordsByPostKeywordVO_Post(Post post);

    @Query(value = "DELETE FROM TBL_POST_KEYWORD WHERE post_no = :postNo", nativeQuery = true)
    void deleteAllByPostNo(@Param("postNo") Long postNo);
}
