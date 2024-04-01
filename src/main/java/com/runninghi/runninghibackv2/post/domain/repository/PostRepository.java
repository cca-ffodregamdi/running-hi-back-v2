package com.runninghi.runninghibackv2.post.domain.repository;

import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreateDateDesc(Pageable pageable);
}
