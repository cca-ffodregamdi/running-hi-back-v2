package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.reply.request.CreateReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.DeleteReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.GetReportedReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.UpdateReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.response.CreateReplyResponse;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.application.dto.reply.response.UpdateReplyResponse;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Reply;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.ReplyQueryRepository;
import com.runninghi.runninghibackv2.domain.repository.ReplyRepository;
import com.runninghi.runninghibackv2.domain.service.ReplyChecker;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.post.domain.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ReplyQueryRepository replyQueryRepository;
    private final ReplyChecker replyChecker;

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /**
     * 게시글 조회 시 해당 게시글에 대한 댓글들 조회 메소드
     * @param postNo 게시글 식별을 위한 키 값
     * @return 댓글들 리스트 ( 댓글 정보, 부모와 자식 댓글 리스트 포함)
     */
    @Transactional(readOnly = true)
    public List<GetReplyListResponse> getReplyList(Long postNo) {

        List<Reply> replyList =  replyRepository.findAllByPost_PostNo(postNo);
        if (replyList.isEmpty()) throw new EntityNotFoundException();


        return replyList.stream()
                .filter(reply -> !reply.isDeleted())
                .map(GetReplyListResponse::fromEntity)
                .toList();
    }


    /**
     * '내가 쓴 댓글들' 혹은 '특정 회원이 쓴 댓글들' 조회 메소드
     * @param memberNo 작성자 식별을 위한 키 값
     * @return 순수 댓글들(부모, 자식 댓글들 제외)
     */
    @Transactional(readOnly = true)
    public List<GetReplyListResponse> getReplyListByMemberNo(Long memberNo) {

        List<Reply> replyList = replyRepository.findAllByWriter_MemberNo(memberNo);
        if (replyList.isEmpty()) throw new EntityNotFoundException();

        return replyList.stream()
                .filter(reply -> !reply.isDeleted())
                .map(GetReplyListResponse::pureReplyListFromEntity)
                .toList();
    }

    /**
     * 댓글 작성 메소드
     * @param request 댓글 작성에 필요한 정보
     * @return 댓글 번호, 작성자 닉네임, 게시글 번호, 댓글 내용, 삭제 여부, 부모 댓글 번호, 생성 일, 수정 일
     */
    @Transactional
    public CreateReplyResponse createReply(CreateReplyRequest request) {

        Member member = memberRepository.findByMemberNo(request.memberNo());
        Post post = postRepository.findById(request.postNo())
                .orElseThrow(EntityNotFoundException::new);

        Reply reply = Reply.builder()
                .writer(member)
                .post(post)
                .replyContent(request.replyContent())
                .build();

        // 부모 댓글 존재 시에
        if (request.parentReplyNo() != null) {
            Reply parentReply = findReplyByReplyNo(request.parentReplyNo());

            parentReply.addChildrenReply(reply);
            reply.addParentReply(parentReply);
        }

        Reply savedReply = replyRepository.save(reply);

        return CreateReplyResponse.fromEntity(savedReply);
    }

    /**
     * 댓글 수정 메소드
     * @param replyNo 댓글 식별을 위한 키 값
     * @param request 회원 식별을 위한 키 값, 수정할 댓글 내용
     * @return  댓글 번호, 작성자 닉네임, 게시글 번호, 댓글 내용, 삭제 여부, 부모 댓글 번호, 생성 일, 수정 일
     */
    @Transactional
    public UpdateReplyResponse updateReply(Long replyNo, UpdateReplyRequest request) {

        Reply reply = findReplyByReplyNo(replyNo);
        checkWriterOrAdmin(request.memberNo(), request.role(), reply);
        reply.update(request.replyContent());

        return UpdateReplyResponse.fromEntity(reply);
    }

    /**
     * 댓글 삭제 메소드 - 댓글 엔티티의 isDeleted의 상태를 'true' 값으로 변경
     * @param request {replyNo, role, memberNo}
     */
    @Transactional
    public void deleteReply(DeleteReplyRequest request) {  // 프론트에서 role 같이 받을 수 있는 지 확인 필요

        Reply reply = findReplyByReplyNo(request.replyNo());
        checkWriterOrAdmin(request.memberNo(), request.role(), reply);
        reply.delete();
    }

    /**
     * 댓글 삭제 메소드 - 댓글 엔티티의 isDeleted의 상태를 'true' 값으로 변경
     * @param replyNo
     */
    @Transactional
    public void deleteReplyById(Long replyNo) {

        Reply reply = findReplyByReplyNo(replyNo);
        reply.delete();
    }

    /**
     * 신고 도메인 측에서 요청하는 메소드.
     * 신고 횟수를 올리는 메소드입니다.
     * @param replyNo
     */
    @Transactional(propagation = Propagation.MANDATORY) // 부모 트랜잭션이 없으면 exception 발생
    public void plusReportedCount (Long replyNo) {

        Reply reply = findReplyByReplyNo(replyNo);
        reply.addReportedCount();
    }

    /**
     * 신고 도메인 측에서 요청하는 메소드.
     * 신고 횟수를 초기화하는 메소드입니다.
     * @param replyNo
     */
    public void resetReportedCount(Long replyNo) {

        Reply reply = findReplyByReplyNo(replyNo);
        reply.resetReportedCount();
    }

    @Transactional(readOnly = true)
    public Page<GetReplyListResponse> getReportedReplyList(GetReportedReplyRequest request) {
        replyChecker.checkSearchValid(request.search());
        return  replyQueryRepository.findAllReportedByPageableAndSearch(request);
    }

    public Reply findReplyByReplyNo (Long replyNo) {

        return replyRepository.findById(replyNo)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void checkWriterOrAdmin (Long memberNo, Role role, Reply reply) {

        boolean checkResult =
                replyChecker.memberCheck(memberNo, role, reply.getWriter().getMemberNo());

        if (!checkResult) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED.getMessage());

    }

}


