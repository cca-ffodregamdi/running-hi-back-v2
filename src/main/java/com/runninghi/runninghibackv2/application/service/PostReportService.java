package com.runninghi.runninghibackv2.application.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.postreport.response.*;
import com.runninghi.runninghibackv2.common.response.PageResult;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.application.dto.postreport.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.domain.entity.PostReport;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.PostReportRepository;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.domain.service.PostReportChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.runninghi.runninghibackv2.domain.entity.QImage.image;

@Service
@RequiredArgsConstructor
public class PostReportService {

    private final PostReportRepository postReportRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostReportChecker postReportChecker;
    private final JPAQueryFactory jpaQueryFactory;

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
                .build();

        postReportRepository.save(postReport);
        reportedPost.addReportedCount();

        return CreatePostReportResponse.from(postReport);
    }

    // 게시글 신고 전체 조회
    @Transactional(readOnly = true)
    public PageResultData<GetAllPostReportsResponse> getPostReports(Pageable pageable) {
        Page<PostReport> postReports = postReportRepository.findAll(pageable);

        List<GetAllPostReportsResponse> responses = postReports.getContent().stream()
                .map(GetAllPostReportsResponse::from)
                .collect(Collectors.toList());

        return new PageResultData<>(responses, pageable, postReports.getTotalElements());
    }

    // 게시글 신고 상세 조회
    @Transactional(readOnly = true)
    public GetPostReportResponse getPostReportById(Long postReportNo) {

        PostReport postReport = postReportRepository.findById(postReportNo)
                .orElseThrow(EntityNotFoundException::new);

        Image mainImage = jpaQueryFactory.select(image)
                .from(image)
                .where(image.postNo.in(postReport.getReportedPost().getPostNo()))
                .limit(1)
                .fetchOne();

        return GetPostReportResponse.from(postReport, mainImage != null ? mainImage.getImageUrl() : null);
    }

    // 신고 처리 상태로 신고된 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> getReportedPostsByStatus(ProcessingStatus status) {

        return postReportRepository.findAllByStatus(status);
    }

    // 신고된 게시글의 모든 신고 내역 조회
    @Transactional(readOnly = true)
    public List<GetAllPostReportsResponse> getPostReportScrollByPostId(Long postNo) {
        Post reportedPost = postRepository.findById(postNo)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postNo));

        List<PostReport> postReports = postReportRepository.findAllByReportedPost(reportedPost);

        return postReports.stream()
                .map(GetAllPostReportsResponse::from)
                .collect(Collectors.toList());
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
            postRepository.deleteById(postNo);

            postReportList.forEach(postReport -> postReport.update(status));
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
