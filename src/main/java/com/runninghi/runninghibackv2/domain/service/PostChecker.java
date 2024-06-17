package com.runninghi.runninghibackv2.domain.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
@Service
public class PostChecker {

    public void checkPostValidation(String content) {
        if (content.isEmpty()) {
            throw new IllegalArgumentException("내용은 1글자 이상이어야 합니다.");
        }
        if (content.isBlank()) {
            throw new IllegalArgumentException("내용은 1글자 이상이어야 합니다.");
        }

    }

    public void isWriter(Long memberNo, Long postWriterNo) {
        if (!postWriterNo.equals(memberNo)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    public boolean isOwner(Long memberNo, Long postWriterNo) {
        if (!postWriterNo.equals(memberNo)) {
            return false;
        }
        return true;
    }
}
