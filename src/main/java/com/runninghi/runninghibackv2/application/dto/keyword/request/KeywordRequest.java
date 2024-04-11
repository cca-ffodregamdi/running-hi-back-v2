package com.runninghi.runninghibackv2.application.dto.keyword.request;

public record KeywordRequest(
        String keywordName

) {
    public String keywordName() {
        return keywordName;
    }
}
