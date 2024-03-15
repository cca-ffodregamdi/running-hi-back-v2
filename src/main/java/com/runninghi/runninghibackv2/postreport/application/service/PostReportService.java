package com.runninghi.runninghibackv2.postreport.application.service;


import com.runninghi.runninghibackv2.common.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;
import com.runninghi.runninghibackv2.postreport.domain.repository.PostReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReportService {

    private PostReportRepository postReportRepository;

    public void createPostReport(CreatePostReportRequest request) {

        PostReport postReport = new PostReport.Builder()
                .category(ReportCategory.INPROGRESS)
                .content(request.content())
                .reportedPostDeleted(false)
                .reporter(request.reporter())
                .reportedMember(request.reportedMember())
                .reportedPost(request.reportedPost())
                .build();

        postReportRepository.save(postReport);

    }
}
