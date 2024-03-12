package com.runninghi.runninghibackv2.reply.application.service;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.reply.application.dto.request.CreateReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.response.CreateReplyResponse;
import com.runninghi.runninghibackv2.reply.application.dto.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import com.runninghi.runninghibackv2.reply.domain.repository.ReplyRepository;
import com.runninghi.runninghibackv2.reply.domain.service.ApiReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ApiReplyService apiReplyService;

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
     * @return 댓글 번호, 작성자 닉네임, 게시글 번호, 댓글 내용, 삭제 여부, 부모 댓글 번호
     */
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
            Reply parentReply = replyRepository.findById(request.parentReplyNo())
                    .orElseThrow( () -> new IllegalArgumentException("검색 결과가 없습니다."));

            parentReply.addChildrenReply(reply);
            reply.addParentReply(parentReply);
        }

        Reply savedReply = replyRepository.save(reply);

        return CreateReplyResponse.fromEntity(savedReply);
    }

}
