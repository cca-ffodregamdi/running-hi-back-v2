package com.runninghi.runninghibackv2.keyword.application.dto.request;

public record KeywordRequest(
        String keywordName

) {
    public String keywordName() {
        return keywordName;
    }
}
