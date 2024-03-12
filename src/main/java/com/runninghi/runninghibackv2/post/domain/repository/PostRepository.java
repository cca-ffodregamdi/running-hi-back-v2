package com.runninghi.runninghibackv2.post.domain.repository;

import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
