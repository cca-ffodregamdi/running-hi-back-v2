package com.runninghi.runninghibackv2.reply.application.service;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.reply.application.dto.request.CreateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.request.UpdateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.response.CreateReplyResponse;
import com.runninghi.runninghibackv2.reply.application.dto.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.reply.application.dto.response.UpdateReplyResponse;
import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import com.runninghi.runninghibackv2.reply.domain.repository.ReplyRepository;
import com.runninghi.runninghibackv2.reply.domain.service.ApiReplyService;
import com.runninghi.runninghibackv2.reply.domain.service.ReplyChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ApiReplyService apiReplyService;
    private final ReplyChecker replyChecker;

    /**
     * 게시글 조회 시 해당 게시글에 대한 댓글들 조회 메소드
     * @param postNo 게시글 식별을 위한 키 값
     * @return 댓글들 리스트 ( 댓글 정보, 부모와 자식 댓글 리스트 포함)
     */
    @Transactional(readOnly = true)
    public List<GetReplyListResponse> getReplyList(Long postNo) {

        List<Reply> replyList =  replyRepository.findAllByPost_PostNo(postNo)
                .orElseThrow( () -> new IllegalArgumentException("검색 결과가 없습니다."));

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

        List<Reply> replyList = replyRepository.findAllByWriter_MemberNo(memberNo)
                .orElseThrow( () -> new IllegalArgumentException("검색 결과가 없습니다."));

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

        Member member = apiReplyService.getMemberByMemberNo(request.memberNo());
        Post post = apiReplyService.getPostByPostNo(request.postNo());

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
        checkWriterOrAdmin(request.memberNo(), reply);

        return UpdateReplyResponse.fromEntity(reply);
    }

    /**
     * 댓글 삭제 메소드 - 댓글 엔티티의 isDeleted의 상태를 'true' 값으로 변경
     * @param replyNo 댓글 식별을 위한 키 값
     * @param memberNo 삭제 요청을 한
     */
    @Transactional
    public void deleteReply(Long replyNo, Long memberNo) {  // 프론트에서 role 같이 받을 수 있는 지 확인 필요

        Reply reply = findReplyByReplyNo(replyNo);
        checkWriterOrAdmin(memberNo, reply);
        reply.delete();
    }

    private Reply findReplyByReplyNo (Long replyNo) {

        return replyRepository.findById(replyNo)
                .orElseThrow( () -> new IllegalArgumentException("검색 결과가 없습니다."));
    }

    private void checkWriterOrAdmin (Long memberNo, Reply reply) {

        Role requestedMemberRole = apiReplyService.getMemberRoleByMemberNo(memberNo);
        boolean checkResult =
                replyChecker.memberCheck(memberNo, requestedMemberRole, reply.getWriter().getMemberNo());

        if (!checkResult) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

}
