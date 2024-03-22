package com.runninghi.runninghibackv2.post.domain.service;

import org.springframework.stereotype.Service;
@Service
public class PostChecker {

    public void checkPostValidation(String title, String content) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("제목은 1글자 이상이어야 합니다.");
        }

        if (content.isEmpty()) {
            throw new IllegalArgumentException("내용은 1글자 이상이어야 합니다.");
        }
        if (title.isBlank()) {
            throw new IllegalArgumentException("제목은 1글자 이상이어야 합니다.");
        }
        if (content.isBlank()) {
            throw new IllegalArgumentException("내용은 1글자 이상이어야 합니다.");
        }

    }
}
