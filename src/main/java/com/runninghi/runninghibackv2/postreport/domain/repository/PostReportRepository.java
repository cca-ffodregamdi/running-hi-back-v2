package com.runninghi.runninghibackv2.postreport.domain.repository;

import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Long> {
}
