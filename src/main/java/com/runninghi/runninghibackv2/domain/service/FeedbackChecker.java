package com.runninghi.runninghibackv2.domain.service;


import com.runninghi.runninghibackv2.domain.enumtype.Role;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class FeedbackChecker {

    // 피드백 수정/삭제 요청자와 피드백 작성자가 동일한지 확인
    public void isWriter(Long memberNo, Long feedbackWriterNo) {
        if (!feedbackWriterNo.equals(memberNo)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    // 요청자가 관리자인지 확인
    public void isAdmin(Role role)  {
        if (!role.equals(Role.ADMIN)) {
            throw new AccessDeniedException("권한이 없습니다 : 관리자만 접근할 수 있습니다.");
        }
    }

    // 피드백 답변이 달렸는지 확인
    public void checkReplyStatus(boolean hasReply) throws BadRequestException {
        if (hasReply) {
            throw new BadRequestException("답변 후에는 수정할 수 없습니다.");
        }
    }

    // 피드백 작성 시 제한 사항 확인
    public void checkFeedbackValidation(String title, String content) throws BadRequestException {
        if (title == null || content == null ||
                title.length() > 500 || title.length() == 0 || content.length() == 0) {
            throw new BadRequestException();
        }
    }


    public void checkFeedbackReplyValidation(String content) throws BadRequestException {
        if (content == null || content.length() == 0) {
            throw new BadRequestException();
        }
    }

}
