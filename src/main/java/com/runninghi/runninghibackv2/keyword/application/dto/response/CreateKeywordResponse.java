package com.runninghi.runninghibackv2.keyword.application.dto.response;

public record CreateKeywordResponse(
        Long keywordNo,
        String keywordName
) {
    public Long getKeywordNo() {
        return keywordNo;
    }

    public String getKeywordName() {
        return keywordName;
    }
}
