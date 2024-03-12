package com.runninghi.runninghibackv2.reply.application.service;

import com.runninghi.runninghibackv2.reply.application.dto.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import com.runninghi.runninghibackv2.reply.domain.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    @Transactional(readOnly = true)
    public List<GetReplyListResponse> getReplyList(Long postNo) {

        List<Reply> replyList =  replyRepository.findAllByPost_PostNo(postNo)
                .orElseThrow( () -> new IllegalArgumentException("검색 결과가 없습니다."));

        return;

    }

}
