package com.runninghi.runninghibackv2.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class PostChecker {

    public void checkPostValidation(String content) {
        if (content.isEmpty()) {
            log.warn("게시글 내용 1글자 이하");
            throw new IllegalArgumentException("내용은 1글자 이상이어야 합니다.");
        }
        if (content.isBlank()) {
            log.warn("게시글 내용 1글자 이하");
            throw new IllegalArgumentException("내용은 1글자 이상이어야 합니다.");
        }

    }

    public void isWriter(Long memberNo, Long postWriterNo) {
        if (!postWriterNo.equals(memberNo)) {
            log.warn("권한 없음: 게시글 작성자와 다른 memberNo. 게시글 작성자: {}, 요청자: {}", postWriterNo, memberNo);
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
