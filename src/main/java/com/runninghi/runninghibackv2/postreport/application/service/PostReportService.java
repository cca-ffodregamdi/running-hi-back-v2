package com.runninghi.runninghibackv2.postreport.application.service;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.response.CreatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;
import com.runninghi.runninghibackv2.postreport.domain.repository.PostReportRepository;
import com.runninghi.runninghibackv2.postreport.domain.service.PostReportChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReportService {

    private final PostReportRepository postReportRepository;
    private final PostReportChecker postReportChecker;

    // 게시글 신고 저장
    public CreatePostReportResponse createPostReport(CreatePostReportRequest request, Member reporter) {

        try {
            postReportChecker.checkPostReportValidation(request, reporter);

            PostReport postReport = new PostReport.Builder()
                    .category(request.category())
                    .content(request.content())
                    .status(ProcessingStatus.INPROGRESS)
                    .reportedPostDeleted(false)
                    .reporter(reporter)
                    .reportedMember(request.reportedMember())
                    .reportedPost(request.reportedPost())
                    .build();

            PostReport insertedPostReport = postReportRepository.save(postReport);

            return CreatePostReportResponse.from(insertedPostReport);
        }
        catch (Exception ex) {
            //TODO. log 추가 후 log 남기도록 리팩토링
            throw ex;
        }
    }
}
