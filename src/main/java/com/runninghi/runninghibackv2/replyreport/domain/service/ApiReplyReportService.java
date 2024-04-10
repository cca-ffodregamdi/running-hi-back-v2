package com.runninghi.runninghibackv2.replyreport.domain.service;

import com.runninghi.runninghibackv2.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.domain.aggregate.entity.Reply;

public interface ApiReplyReportService {
    Member getMemberById(Long memberNo);
    Reply getReplyById(Long replyNo);
    void deleteReplyById(Long replyNo);
    void addReportedCountToMember(Long memberNo);
    void addReportedCountToReply(Long replyNo);
    void resetReportedCountOfReply(Long replyNo);
}
