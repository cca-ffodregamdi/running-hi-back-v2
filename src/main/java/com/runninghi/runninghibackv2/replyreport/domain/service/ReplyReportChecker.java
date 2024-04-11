package com.runninghi.runninghibackv2.replyreport.domain.service;

import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.replyreport.application.dto.request.CreateReplyReportRequest;
import org.springframework.stereotype.Service;

@Service
public class ReplyReportChecker {
    public void checkReplyReportValidation(CreateReplyReportRequest request) {
        
        if(request.category() == null) {
            throw new IllegalArgumentException("댓글 신고 저장: 신고 유형이 선택되지 않았습니다.");
        }

        if(request.category() == ReportCategory.OTHER && request.content() == null) {
            throw new IllegalArgumentException("댓글 신고 저장: 기타 신고 사유가 입력되지 않았습니다.");
        }

        if(request.content() != null && request.content().trim().length() < 10) {
            throw new IllegalArgumentException("댓글 신고 저장: 기타 신고 사유는 10자 이상 입력해야 합니다.");
        }

        if(request.content() != null && request.content().length() > 100) {
            throw new IllegalArgumentException("댓글 신고 저장: 기타 신고 사유는 100자를 넘을 수 없습니다.");
        }

        if(request.reportedReplyNo() == null) {
            throw new IllegalArgumentException("댓글 신고 저장: 신고된 댓글 정보가 없습니다.");
        }
    }
}
