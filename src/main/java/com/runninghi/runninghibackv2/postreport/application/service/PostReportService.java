package com.runninghi.runninghibackv2.postreport.application.service;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.postreport.application.dto.request.UpdatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.response.UpdatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.dto.response.GetPostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.response.CreatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;
import com.runninghi.runninghibackv2.postreport.domain.repository.PostReportRepository;
import com.runninghi.runninghibackv2.postreport.domain.service.ApiPostReportService;
import com.runninghi.runninghibackv2.postreport.domain.service.PostReportChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostReportService {

    private final PostReportRepository postReportRepository;
    private final PostReportChecker postReportChecker;
    private final ApiPostReportService apiPostReportService;

    // 게시글 신고 저장
    @Transactional
    public CreatePostReportResponse createPostReport(CreatePostReportRequest request, Long reporterNo) {

        try {
            postReportChecker.checkPostReportValidation(request);

            Member reporter = apiPostReportService.getMemberById(reporterNo);
            Member reportedMember = apiPostReportService.getMemberById(request.reportedMemberNo());
            Post reportedPost = apiPostReportService.getPostById(request.reportedPostNo());

            PostReport postReport = new PostReport.Builder()
                    .category(request.category())
                    .content(request.content())
                    .status(ProcessingStatus.INPROGRESS)
                    .reportedPostDeleted(false)
                    .reporter(reporter)
                    .reportedMember(reportedMember)
                    .reportedPost(reportedPost)
                    .build();

            PostReport insertedPostReport = postReportRepository.save(postReport);

            return CreatePostReportResponse.from(insertedPostReport);
        }
        catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public GetPostReportResponse getPostReportById(Long postReportNo) {

        try {
            PostReport postReport = postReportRepository.findById(postReportNo)
                    .orElseThrow(() -> new EntityNotFoundException());

            return GetPostReportResponse.from(postReport);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public UpdatePostReportResponse updatePostReport(UpdatePostReportRequest request, Long postReportNo) {

        try {
            PostReport postReport = postReportRepository.findById(postReportNo)
                    .orElseThrow(() -> new EntityNotFoundException());

            postReport.update(request);
            PostReport updatedPostReport = postReportRepository.save(postReport);

            return UpdatePostReportResponse.from(updatedPostReport);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void deletePostReport(Long postReportNo) {

        try {
            postReportRepository.findById(postReportNo)
                    .orElseThrow(() -> new EntityNotFoundException());

            postReportRepository.deleteById(postReportNo);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
