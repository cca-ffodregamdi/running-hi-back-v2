package com.runninghi.runninghibackv2.feedback.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackDomainService {

    // 피드백 수정/삭제 요청자와 피드백 작성자가 동일한지 확인

    // 요청자가 관리자인지 확인

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
