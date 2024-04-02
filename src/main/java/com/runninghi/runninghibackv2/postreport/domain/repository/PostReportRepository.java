package com.runninghi.runninghibackv2.postreport.domain.repository;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    @Query("SELECT pr.reportedPost FROM PostReport pr WHERE pr.status = :status")
    List<Post> findReportedPostsByStatus(@Param("status") ProcessingStatus status);

    @Query("SELECT pr FROM PostReport pr WHERE pr.reportedPost.postNo = :postNo")
    List<PostReport> findPostReportsByPostId(@Param("postNo") Long postNo);

    @Query("SELECT pr FROM PostReport pr WHERE pr.reportedPost.postNo = :postNo ORDER BY pr.createDate DESC")
    Page<PostReport> findPostReportScrollByPostId(@Param("postNo") Long postNo, Pageable pageable);
}