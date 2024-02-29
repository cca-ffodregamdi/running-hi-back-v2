package com.runninghi.runninghibackv2.commentreport.domain.repository;

import com.runninghi.runninghibackv2.commentreport.domain.aggregate.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
}
