package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.replyreport.request.CreateReplyReportRequest;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.Reply;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.domain.repository.ReplyReportRepository;
import com.runninghi.runninghibackv2.domain.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ReplyReportServiceTests {

    @Autowired
    private ReplyReportService replyReportService;
    @Autowired
    private ReplyReportRepository replyReportRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    private Member admin;
    private Member reporter;
    private Member reportedMember;
    private Post reportedPost;
    private Reply reportedReply;

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

        reportedReply = Reply.builder()
                .member(reportedMember)
                .post(reportedPost)
                .replyContent("댓글 테스트 내용")
                .reportedCount(0)
                .isDeleted(false)
                .build();

        memberRepository.saveAndFlush(admin);
        memberRepository.saveAndFlush(reporter);
        memberRepository.saveAndFlush(reportedMember);
        postRepository.saveAndFlush(reportedPost);
        replyRepository.saveAndFlush(reportedReply);
    }

//    @Test
//    @DisplayName("댓글 신고 저장: 저장 성공")
//    void testCreatePostReportSuccess() {
//
//        // given
//        long beforeCount = replyReportRepository.count();
//
//        CreateReplyReportRequest request = new CreateReplyReportRequest(
//                ReportCategory.ILLEGAL,
//                null,
//                reportedReply.getReplyNo()
//        );
//
//        // when
//        replyReportService.createReplyReport(reporter.getMemberNo(), request);
//
//        long afterCount = replyReportRepository.count();
//
//        // then
//        assertEquals(1, afterCount - beforeCount);
//    }
}
