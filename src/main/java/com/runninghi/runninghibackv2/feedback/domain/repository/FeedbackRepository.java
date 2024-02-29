package com.runninghi.runninghibackv2.feedback.domain.repository;

import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
