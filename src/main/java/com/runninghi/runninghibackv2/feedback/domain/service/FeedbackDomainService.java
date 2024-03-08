package com.runninghi.runninghibackv2.feedback.domain.service;


import com.runninghi.runninghibackv2.common.entity.Role;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class FeedbackDomainService {

    // 피드백 수정/삭제 요청자와 피드백 작성자가 동일한지 확인
    public void isWriter(Long memberNo, Long feedbackWriterNo) throws BadRequestException {
        if (!feedbackWriterNo.equals(memberNo)) {
            throw new BadRequestException("피드백을 업데이트할 권한이 없습니다.");
        }
    }

    // 요청자가 관리자인지 확인
    public void isAdmin(Role role) throws AuthenticationException {
        if (!role.equals(Role.ADMIN)) {
            throw new AuthenticationException("권한이 없습니다 : 관리자만 접근할 수 있습니다.");
        }
    }

    // 피드백 답변이 달렸는지 확인
    public void checkReplyStatus(boolean hasReply) throws BadRequestException {
        if (hasReply) {
            throw new BadRequestException("답변 후에는 수정할 수 없습니다.");
        }
    }

    // 피드백 작성 시 제한 사항 확인
    public void checkFeedbackValidation(String title, String content) {

        if (title.length() > 500) {
            throw new IllegalArgumentException("제목은 500자를 넘을 수 없습니다.");
        }

        if (title.length() == 0) {
            throw new IllegalArgumentException("제목은 1글자 이상이어야 합니다.");
        }

        if (content.length() == 0) {
            throw new IllegalArgumentException("내용은 1글자 이상이어야 합니다.");
        }

    }

}
