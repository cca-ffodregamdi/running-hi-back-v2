package com.runninghi.runninghibackv2.application.dto.post.request;

import java.util.List;

public record PostKeywordCriteria (
        int page,
        int size,
        List<String> keyword
){
}
