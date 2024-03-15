package com.runninghi.runninghibackv2.post.application.dto.request;

import java.util.List;

public record UpdatePostRequest(
        String postTitle,
        String postContent,
        List<String> keywordList
) {
}
