package com.runninghi.runninghibackv2.postreport.domain.service;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import org.springframework.stereotype.Service;

@Service
public class PostReportChecker {

    public void checkPostReportValidation(CreatePostReportRequest request) {

        if(request.category() == null) {
            throw new IllegalArgumentException("저장: 신고 유형은 필수 선택입니다.");
        }

        if(request.content().length() == 0) {
            // TODO. 신고 유형이 '기타'인 경우 기타 신고사유 null 검증
        }

        if(request.content().length() > 100) {
            throw new IllegalArgumentException("저장: 기타 신고사유는 100자를 넘을 수 없습니다.");
        }

        if(request.reportedMemberNo() == null) {
            throw new IllegalArgumentException("저장: 피신고자 정보가 없습니다.");
        }

        if(request.reportedPostNo() == null) {
            throw new IllegalArgumentException("저장: 신고된 게시글 정보가 없습니다.");
        }
    }
}