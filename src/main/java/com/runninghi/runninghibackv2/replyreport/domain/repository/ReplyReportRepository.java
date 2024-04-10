package com.runninghi.runninghibackv2.replyreport.domain.repository;

import com.runninghi.runninghibackv2.domain.aggregate.entity.Reply;
import com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity.ReplyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {

    Page<ReplyReport> findAllByReportedReply(Reply reply, Pageable pageable);
    List<ReplyReport> findAllByReportedReply(Reply reply);
}
