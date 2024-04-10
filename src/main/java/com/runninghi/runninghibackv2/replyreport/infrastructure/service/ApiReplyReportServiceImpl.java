package com.runninghi.runninghibackv2.replyreport.infrastructure.service;

import com.runninghi.runninghibackv2.service.MemberService;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.reply.application.service.ReplyService;
import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import com.runninghi.runninghibackv2.replyreport.domain.service.ApiReplyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiReplyReportServiceImpl implements ApiReplyReportService {

    private final MemberService memberService;
    private final ReplyService replyService;

    @Override
    public Member getMemberById(Long memberNo) {
        return memberService.findMemberByNo(memberNo);
    }

    @Override
    public Reply getReplyById(Long replyNo) {
        return replyService.findReplyByReplyNo(replyNo);
    }

    @Override
    public void deleteReplyById(Long replyNo) {
        replyService.deleteReplyById(replyNo);
    }

    @Override
    public void addReportedCountToMember(Long memberNo) {
        memberService.addReportedCount(memberNo);
    }

    @Override
    public void addReportedCountToReply(Long replyNo) {
        replyService.plusReportedCount(replyNo);
    }

    @Override
    public void resetReportedCountOfReply(Long replyNo) {
        replyService.resetReportedCount(replyNo);
    }
}
