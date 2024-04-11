package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.postreport.response.DeletePostReportResponse;
import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.application.dto.postreport.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.application.dto.postreport.response.CreatePostReportResponse;
import com.runninghi.runninghibackv2.application.dto.postreport.response.GetPostReportResponse;
import com.runninghi.runninghibackv2.application.dto.postreport.response.HandlePostReportResponse;
import com.runninghi.runninghibackv2.domain.entity.PostReport;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.PostReportRepository;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.domain.service.PostReportChecker;
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
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostReportChecker postReportChecker;

    // 게시글 신고 저장
    @Transactional
    public CreatePostReportResponse createPostReport(Long memberNo, CreatePostReportRequest request) {

        postReportChecker.checkPostReportValidation(request);

        Member reporter = memberRepository.findByMemberNo(memberNo);
        Post reportedPost = postRepository.findById(request.reportedPostNo())
                .orElseThrow(EntityNotFoundException::new);

        PostReport postReport = PostReport.builder()
                .category(request.category())
                .content(request.content())
                .status(ProcessingStatus.INPROGRESS)
                .reporter(reporter)
                .reportedPost(reportedPost)
                .postContent(reportedPost.getPostContent())
                .isPostDeleted(false)
                .build();

        postReportRepository.save(postReport);
        reportedPost.addReportedCount();

        return CreatePostReportResponse.from(postReport);
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

        return postReportRepository.findAllByStatus(status);
    }

    // 신고된 게시글의 모든 신고 내역 조회
    @Transactional(readOnly = true)
    public Page<GetPostReportResponse> getPostReportScrollByPostId(Long postNo, Pageable pageable) {

        Post reportedPost = postRepository.findById(postNo).orElseThrow(EntityNotFoundException::new);

        return postReportRepository.findAllByReportedPost(reportedPost, pageable)
                .map(GetPostReportResponse::from);
    }

    // 게시글 신고 수락/거절 처리
    @Transactional
    public List<HandlePostReportResponse> handlePostReports(boolean isAccepted, Long postNo) {

        Post reportedPost = postRepository.findById(postNo).orElseThrow(EntityNotFoundException::new);
        List<PostReport> postReportList = postReportRepository.findAllByReportedPost(reportedPost);
        ProcessingStatus status;

        if(isAccepted) {
            Member reportedMember = reportedPost.getMember();
            reportedMember.addReportedCount();
            status = ProcessingStatus.ACCEPTED;

            postReportList.forEach(postReport -> postReport.update(status, true, null));
            postRepository.deleteById(postNo);
        } else {
            status = ProcessingStatus.REJECTED;
            reportedPost.resetReportedCount();
            postReportList.forEach(postReport -> postReport.update(status));
        }

        return postReportList.stream()
                .map(HandlePostReportResponse::from)
                .toList();
    }

    // 게시글 신고 삭제
    @Transactional
    public DeletePostReportResponse deletePostReport(Long postReportNo) {

        postReportRepository.deleteById(postReportNo);

        return DeletePostReportResponse.from(postReportNo);
    }
}
