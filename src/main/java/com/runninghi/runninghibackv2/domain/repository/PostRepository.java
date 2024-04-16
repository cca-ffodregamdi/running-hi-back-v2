package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreateDateDesc(Pageable pageable);

    Page<Post> findAllByReportCntIsGreaterThan(int reportCnt, Pageable pageable);
}
