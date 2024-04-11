package com.runninghi.runninghibackv2.postreport.application.service;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.response.CreatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.dto.response.GetPostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.dto.response.HandlePostReportResponse;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;
import com.runninghi.runninghibackv2.postreport.domain.repository.PostReportRepository;
import com.runninghi.runninghibackv2.postreport.domain.service.ApiPostReportService;
import com.runninghi.runninghibackv2.postreport.domain.service.PostReportChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReportService {

    private final PostReportRepository postReportRepository;
    private final PostReportChecker postReportChecker;
    private final ApiPostReportService apiPostReportService;

    // 게시글 신고 저장
    @Transactional
    public CreatePostReportResponse createPostReport(Long memberNo, CreatePostReportRequest request) {

        postReportChecker.checkPostReportValidation(request);

        Member reporter = apiPostReportService.getMemberById(memberNo);
        Post reportedPost = apiPostReportService.getPostById(request.reportedPostNo());

        PostReport postReport = new PostReport.Builder()
                .category(request.category())
                .content(request.content())
                .status(ProcessingStatus.INPROGRESS)
                .reporter(reporter)
                .reportedPost(reportedPost)
                .isPostDeleted(false)
                .build();

        PostReport insertedPostReport = postReportRepository.save(postReport);
        apiPostReportService.addReportedCountToPost(reportedPost.getPostNo());

        return CreatePostReportResponse.from(insertedPostReport);
    }

    // 게시글 신고 전체 조회
    @Transactional(readOnly = true)
    public List<GetPostReportResponse> getPostReports() {

        return postReportRepository.findAll().stream()
                .map(GetPostReportResponse::from)
                .toList();
    }

    // 게시글 신고 상세 조회
    @Transactional(readOnly = true)
    public GetPostReportResponse getPostReportById(Long postReportNo) {

        PostReport postReport = postReportRepository.findById(postReportNo)
                .orElseThrow(EntityNotFoundException::new);

        return GetPostReportResponse.from(postReport);
    }

    // 신고 처리 상태로 신고된 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> getReportedPostsByStatus(ProcessingStatus status) {

        return postReportRepository.findReportedPostsByStatus(status);
    }

    // 신고된 게시글의 모든 신고 내역 조회
    @Transactional(readOnly = true)
    public Page<GetPostReportResponse> getPostReportScrollByPostId(Long postNo, Pageable pageable) {

        return postReportRepository.findPostReportScrollByPostId(postNo, pageable)
                .map(GetPostReportResponse::from);
    }

    // 게시글 신고 수락/거절 처리
    @Transactional
    public List<HandlePostReportResponse> handlePostReports(boolean isAccepted, Long postNo) {

        List<PostReport> postReportList = postReportRepository.findPostReportsByPostId(postNo);
        ProcessingStatus status;

        if(isAccepted) {
            Long reportedMemberNo = apiPostReportService.getPostById(postNo).getMember().getMemberNo();
            apiPostReportService.addReportedCountToMember(reportedMemberNo);
            status = ProcessingStatus.ACCEPTED;

            postReportList.forEach(postReport -> postReport.update(status, true, null));
            apiPostReportService.deletePostById(postNo);
        } else {
            status = ProcessingStatus.REJECTED;
            apiPostReportService.resetReportedCountOfPost(postNo);
            postReportList.forEach(postReport -> postReport.update(status));
        }

        return postReportList.stream()
                .map(HandlePostReportResponse::from)
                .toList();
    }

    // 게시글 신고 삭제
    @Transactional
    public void deletePostReport(Long postReportNo) {

        postReportRepository.findById(postReportNo)
                .orElseThrow(EntityNotFoundException::new);

        postReportRepository.deleteById(postReportNo);
    }
}
