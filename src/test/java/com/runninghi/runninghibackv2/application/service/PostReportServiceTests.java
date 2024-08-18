package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.postreport.request.CreatePostReportRequest;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.PostReportRepository;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PostReportServiceTests {

    @Autowired
    private PostReportService postReportService;
    @Autowired
    private PostReportRepository postReportRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    private Member admin;
    private Member reporter;
    private Member reportedMember;
    private Post reportedPost;

    @BeforeEach
    void setup() {

        admin = Member.builder()
                .nickname("admin01")
                .isBlacklisted(false)
                .isActive(true)
                .role(Role.ADMIN)
                .build();

        reporter = Member.builder()
                .nickname("user01")
                .isBlacklisted(false)
                .isActive(true)
                .role(Role.USER)
                .build();

        reportedMember = Member.builder()
                .nickname("user02")
                .isBlacklisted(false)
                .isActive(true)
                .role(Role.USER)
                .build();

        reportedPost = Post.builder()
                .member(reportedMember)
                .postContent("content")
                .role(Role.USER)
                .locationName("서울")
                .build();

        memberRepository.saveAndFlush(admin);
        memberRepository.saveAndFlush(reporter);
        memberRepository.saveAndFlush(reportedMember);
        postRepository.saveAndFlush(reportedPost);
    }

    @Test
    @DisplayName("게시글 신고 저장: 저장 성공")
    void testCreatePostReportSuccess() {

        // given
        long beforeCount = postReportRepository.count();

        CreatePostReportRequest request = new CreatePostReportRequest(
                ReportCategory.SPAM,
                null,
                reportedPost.getPostNo()
        );

        // when
        postReportService.createPostReport(reporter.getMemberNo(), request);

        long afterCount = postReportRepository.count();

        // then
        assertEquals(1, afterCount - beforeCount);
    }

    @Test
    @DisplayName("게시글 신고 저장: 신고 유형 null인 경우 예외처리")
    void testReportCategoryIsNull() {

        // given
        CreatePostReportRequest request = new CreatePostReportRequest(
                null,
                null,
                reportedPost.getPostNo()
        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postReportService.createPostReport(reporter.getMemberNo(), request));

        assertEquals("게시글 신고 저장: 신고 유형이 선택되지 않았습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("게시글 신고 저장: 신고 유형 '기타'이고 기타 신고 사유 null인 경우 예외처리")
    void testOtherReportContentIsNull() {

        // given
        CreatePostReportRequest request = new CreatePostReportRequest(
                ReportCategory.OTHER,
                null,
                reportedPost.getPostNo()
        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postReportService.createPostReport(reporter.getMemberNo(), request));

        assertEquals("게시글 신고 저장: 기타 신고 사유가 입력되지 않았습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("게시글 신고 저장: 신고된 게시글 정보 null인 경우 예외처리")
    void testReportedPostIsNull() {

        // given
        CreatePostReportRequest request = new CreatePostReportRequest(
                ReportCategory.SPAM,
                null,
                null
        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postReportService.createPostReport(reporter.getMemberNo(), request));

        assertEquals("게시글 신고 저장: 신고된 게시글 정보가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("게시글 신고 저장: 기타 신고 사유가 공백을 제외하고 10자 미만인 경우 예외처리")
    void testContentMinLength() {

        // given
        CreatePostReportRequest request = new CreatePostReportRequest(
                ReportCategory.OTHER,
                "신고합니다       ",
                reportedPost.getPostNo()
        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postReportService.createPostReport(reporter.getMemberNo(), request));

        assertEquals("게시글 신고 저장: 기타 신고 사유는 10자 이상 입력해야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("게시글 신고 저장: 기타 신고 사유가 100자 초과인 경우 예외처리")
    void testContentMaxLength() {

        // given
        String content = "a".repeat(101);

        CreatePostReportRequest request = new CreatePostReportRequest(
                ReportCategory.OTHER,
                content,
                reportedPost.getPostNo()
        );

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postReportService.createPostReport(reporter.getMemberNo(), request));

        assertEquals("게시글 신고 저장: 기타 신고 사유는 100자를 넘을 수 없습니다.", exception.getMessage());
    }

//    @Test
//    @DisplayName("게시글 신고 조회: 전체 조회")
//    void testGetPostReports() {
//
//        // given
//        CreatePostReportRequest request = new CreatePostReportRequest(
//                ReportCategory.SPAM,
//                null,
//                reportedPost.getPostNo()
//        );
//
//        postReportService.createPostReport(reporter.getMemberNo(), request);
//
//        // when & then
//        assertEquals(postReportRepository.count(), postReportService.getPostReports().size());
//    }
}
