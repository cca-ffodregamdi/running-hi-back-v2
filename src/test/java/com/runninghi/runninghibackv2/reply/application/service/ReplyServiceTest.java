package com.runninghi.runninghibackv2.reply.application.service;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.post.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.reply.application.dto.request.UpdateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.reply.application.dto.response.UpdateReplyResponse;
import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import com.runninghi.runninghibackv2.reply.domain.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReplyServiceTest {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private Member member1;
    private Member member2;
    private Member admin;
    private Reply reply1;
    private Reply reply2;
    private Reply reply3;
    private Reply reply4;
    private Post post;

    @BeforeEach
    @AfterEach
    void clear() {
        replyRepository.deleteAllInBatch();
    }

    @BeforeEach
    void setup() {

        member1 = new Member.Builder()
                .nickname("테스트멤버1")
                .role(Role.USER)
                .build();

        member2 = new Member.Builder()
                .nickname("테스트멤버2")
                .role(Role.USER)
                .build();

        admin = new Member.Builder()
                .nickname("관리자")
                .role(Role.ADMIN)
                .build();

        post = Post.builder()
                .member(member1)
                .postTitle("테스트 게시글")
                .postContent("테스트 게시글 내용")
                .role(Role.USER)
                .build();

        reply1 = Reply.builder()
                .writer(member1)
                .post(post)
                .replyContent("댓글 테스트 내용")
                .reportedCount(0)
                .isDeleted(false)
                .build();

        reply2 = Reply.builder()
                .writer(member2)
                .post(post)
                .replyContent("댓글 테스트 내용2")
                .reportedCount(1)
                .isDeleted(false)
                .build();

        reply3 = Reply.builder()
                .writer(member1)
                .post(post)
                .replyContent("댓글 테스트 내용3")
                .reportedCount(0)
                .isDeleted(false)
                .build();

        reply4 = Reply.builder()
                .writer(member1)
                .post(post)
                .replyContent("댓글 테스트 내용4")
                .reportedCount(4)
                .isDeleted(true)
                .build();

        List<Member> setupMemberList = Arrays.asList( member1, member2, admin);
        List<Reply> setupReplyList = Arrays.asList(reply1, reply2, reply3, reply4);

        memberRepository.saveAllAndFlush(setupMemberList);
        postRepository.saveAndFlush(post);
        replyRepository.saveAllAndFlush(setupReplyList);
    }

    /* Read */
    @Test
    @DisplayName("댓글 조회 테스트 : 게시글 조회 시 관련 댓글들 리스트 조회, 삭제된 댓글은 제외되는 지 확인")
    void testGetReplyList() {

        // when
        List<GetReplyListResponse> response = replyService.getReplyList(post.getPostNo());

        // then
        Assertions.assertThat(response)
                .isNotEmpty()
                .hasSize(3)
                .extracting("memberName", String.class)
                .contains("테스트멤버1", "테스트멤버2");
    }

    @ParameterizedTest
    @DisplayName("댓글 조회 테스트 : 게시글 엔티티 조회 실패 시 예외 발생 확인")
    @NullSource
    @ValueSource(longs = 3)
    void testGetReplyListException (Long postNo) {

        // when & then
        Assertions.assertThatThrownBy(() -> replyService.getReplyList(postNo))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("검색 결과가 없습니다.");
    }

    @Test
    @DisplayName("댓글 조회 테스트 : 특정 회원의 쓴 댓글들 리스트 조회, 삭제된 댓글 제외되는 지 확인")
    void testGetReplyListByMemberNo () {

        // given
        List<String> expected = Arrays.asList("테스트멤버1", "테스트멤버1");

        // when
        List<GetReplyListResponse> response = replyService.getReplyListByMemberNo(member1.getMemberNo());

        // then
        Assertions.assertThat(response)
                .isNotEmpty()
                .hasSize(2)
                .extracting("memberName", String.class)
                .isEqualTo(expected);
    }

//    @Test
//    @DisplayName("댓글 생성 테스트 : success")
//    void testCreateReplyTest () {
//
//        // given
//        CreateReplyRequest request = new CreateReplyRequest(
//                member1.getMemberNo(),
//                post.getPostNo(),
//                "새로운 댓글",
//                null
//        );
//        long beforeSize = replyRepository.count();
//
//        // when & then
//        replyService.createReply(request);
//        long afterSize = replyRepository.count();
//
//        // then
//        Assertions.assertThat(afterSize)
//                .isEqualTo(beforeSize+1);
//
//
//    }

//    @Test
//    @DisplayName("댓글 생성 테스트 : 부모 댓글 존재 시 엔티티 내부 부모 댓글 컬럼 확인")
//    void testParentReply () {
//
//        // given
//        CreateReplyRequest request = new CreateReplyRequest(
//                member1.getMemberNo(),
//                post.getPostNo(),
//                "새로운 댓글",
//                1L
//        );
//
//        // when
//        CreateReplyResponse response = replyService.createReply(request);
//
//        // then
//        Assertions.assertThat(response)
//                .extracting("parentReplyNo")
//                .isEqualTo(request.parentReplyNo());
//    }

    @Test
    @DisplayName("댓글 수정 테스트 : 수정 메소드 정상 작동 확인")
    void testUpdateReply() {

        // given
        Long replyNo = reply1.getReplyNo();
        UpdateReplyRequest request = new UpdateReplyRequest(member1.getMemberNo(), "수정된 댓글");

        // when
        UpdateReplyResponse response = replyService.updateReply(replyNo, request);

        // then
        Assertions.assertThat(response)
                .extracting("replyContent")
                .isEqualTo("수정된 댓글");
    }

    @Test
    @DisplayName("댓글 수정 테스트 : 댓글 생성자 혹은 관리자가 아닌 제 3자가 수정 요청 시 예외처리 확인")
    void testAnotherMemberRequestUpdate () {

        // given
        Long replyNo = reply1.getReplyNo();
        UpdateReplyRequest request = new UpdateReplyRequest(member2.getMemberNo(), "수정한 내용");

        // when
        Assertions.assertThatThrownBy(
                () -> replyService.updateReply(replyNo, request)
        ).isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("권한이 없습니다.");
    }

    @Test
    @DisplayName("댓글 삭제 테스트 : 댓글 삭제 메소드 정상 작동 확인")
    void testDeleteReply() {

        // given
        Long replyNo = reply1.getReplyNo();
        Long memberNo = reply1.getWriter().getMemberNo();

        // when
        replyService.deleteReply(replyNo, memberNo);

        // then
        Assertions.assertThat(reply1)
                .extracting("isDeleted")
                .isEqualTo(true);

    }

    @Test
    @DisplayName("댓글 삭제 테스트 : 제 3자의 유저가 삭제 요청 시 예외 발생 확인")
    void testAnotherMemberRequestDelete () {

        // given
        Long replyNo = reply1.getReplyNo();
        Long memberNo = member2.getMemberNo();

        // when & then
        Assertions.assertThatThrownBy(
                () -> replyService.deleteReply(replyNo, memberNo)
        ).isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("권한이 없습니다.");
    }



}