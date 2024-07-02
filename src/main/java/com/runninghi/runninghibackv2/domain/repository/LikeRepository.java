package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Like;
import com.runninghi.runninghibackv2.domain.entity.vo.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

    Integer countByPost_PostNo(Long postNo);
}
