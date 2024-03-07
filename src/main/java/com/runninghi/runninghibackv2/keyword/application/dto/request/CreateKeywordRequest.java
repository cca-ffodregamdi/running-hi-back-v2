package com.runninghi.runninghibackv2.keyword.application.dto.request;

public record CreateKeywordRequest(
        String keywordName

) {
    public String keywordName() {
        return keywordName;
    }
}
