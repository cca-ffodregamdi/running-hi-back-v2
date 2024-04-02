package com.runninghi.runninghibackv2.replyreport.domain.repository;

import com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReplyReportRepository extends JpaRepository<CommentReport, Long> {
}
