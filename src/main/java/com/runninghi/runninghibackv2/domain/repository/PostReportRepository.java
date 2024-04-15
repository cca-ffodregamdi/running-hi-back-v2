package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.PostReport;
import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    List<Post> findAllByStatus(ProcessingStatus status);
    Page<PostReport> findAllByReportedPost(Post post, Pageable pageable);
    List<PostReport> findAllByReportedPost(Post post);
}