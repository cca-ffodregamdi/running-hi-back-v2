package com.runninghi.runninghibackv2.postreport.domain.service;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.postreport.application.dto.request.CreatePostReportRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class PostReportChecker {

    public void checkPostReportValidation(CreatePostReportRequest request) {

        if(request.category() == null) {
            throw new IllegalArgumentException("게시글 신고 저장: 신고 유형이 선택되지 않았습니다.");
        }

        if(request.category() == ReportCategory.OTHER && request.content() == null) {
            throw new IllegalArgumentException("게시글 신고 저장: 기타 신고 사유가 입력되지 않았습니다.");
        }

        if(request.content() != null && request.content().trim().length() < 10) {
            throw new IllegalArgumentException("게시글 신고 저장: 기타 신고 사유는 10자 이상 입력해야 합니다.");
        }

        if(request.content() != null && request.content().length() > 100) {
            throw new IllegalArgumentException("게시글 신고 저장: 기타 신고 사유는 100자를 넘을 수 없습니다.");
        }

        if(request.reportedPostNo() == null) {
            throw new IllegalArgumentException("게시글 신고 저장: 신고된 게시글 정보가 없습니다.");
        }
    }

    public void isAdmin(Role role)  {
        if (!role.equals(Role.ADMIN)) {
            throw new AccessDeniedException("권한이 없습니다: 관리자만 접근할 수 있습니다.");
        }
    }
}