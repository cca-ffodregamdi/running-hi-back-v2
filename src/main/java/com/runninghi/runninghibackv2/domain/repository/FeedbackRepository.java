package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Feedback;
import com.runninghi.runninghibackv2.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findAllByFeedbackWriter(Member member, Pageable pageable);

    Page<Feedback> findAllBy(Pageable pageable);

    List<Feedback> findAllByFeedbackWriter(Member deactivateMember);

    void deleteAllByFeedbackWriter(Member deactivateMemberNo);
}
