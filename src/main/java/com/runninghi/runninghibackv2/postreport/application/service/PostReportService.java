package com.runninghi.runninghibackv2.postreport.application.service;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.request.UpdatePostReportRequest;
import com.runninghi.runninghibackv2.postreport.application.dto.response.CreatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.dto.response.GetPostReportResponse;
import com.runninghi.runninghibackv2.postreport.application.dto.response.UpdatePostReportResponse;
import com.runninghi.runninghibackv2.postreport.domain.aggregate.entity.PostReport;
import com.runninghi.runninghibackv2.postreport.domain.repository.PostReportRepository;
import com.runninghi.runninghibackv2.postreport.domain.service.ApiPostReportService;
import com.runninghi.runninghibackv2.postreport.domain.service.PostReportChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new EntityNotFoundException());

        return GetPostReportResponse.from(postReport);
    }

    // 신고 처리 상태로 신고된 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> getReportedPostsByStatus(String status) {

        // TODO. enum validation
        ProcessingStatus processingStatus = ProcessingStatus.valueOf(status);

        return postReportRepository.findReportedPostsByStatus(processingStatus);
    }

    // 신고된 게시글의 모든 신고 내역 조회
    @Transactional(readOnly = true)
    public List<GetPostReportResponse> getPostReportsByPostId(Long postNo) {

        return postReportRepository.findPostReportsByPostId(postNo).stream()
                .map(GetPostReportResponse::from)
                .toList();
    }

    // 게시글 신고 수락/거절 처리(업데이트)
    @Transactional
    public List<UpdatePostReportResponse> updatePostReport(UpdatePostReportRequest request, Long postNo) {

        List<PostReport> postReportList = postReportRepository.findPostReportsByPostId(postNo);
        ProcessingStatus status;

        if(request.isAccepted()) {
            Long reportedMemberNo = apiPostReportService.getPostById(postNo).getMember().getMemberNo();
            apiPostReportService.addReportedCountToMember(reportedMemberNo);
            status = ProcessingStatus.ACCEPTED;

            // TODO. post삭제가 아닌 isDeleted 필드 변경하는 방법으로 리팩토링
            // apiPostReportService.deletePostById(postNo);
        } else {
            status = ProcessingStatus.REJECTED;
            apiPostReportService.resetReportedCountOfPost(postNo);
        }

        postReportList.forEach(postReport -> postReport.update(status, request.isAccepted()));

        return postReportList.stream()
                .map(UpdatePostReportResponse::from)
                .toList();
    }

    // 게시글 신고 삭제
    @Transactional
    public void deletePostReport(Long postReportNo) {

        postReportRepository.findById(postReportNo)
                .orElseThrow(() -> new EntityNotFoundException());

        postReportRepository.deleteById(postReportNo);
    }
}
