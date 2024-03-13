package com.runninghi.runninghibackv2.post.domain.service;

import org.springframework.stereotype.Service;
@Service
public class PostChecker {

    public void checkPostValidation(String title, String content) {

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
