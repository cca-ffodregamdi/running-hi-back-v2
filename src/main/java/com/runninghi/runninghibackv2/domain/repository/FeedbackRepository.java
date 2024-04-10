package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.aggregate.entity.Feedback;
import com.runninghi.runninghibackv2.domain.aggregate.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findAllByFeedbackWriter(Member member, Pageable pageable);

    Page<Feedback> findAllBy(Pageable pageable);

}
