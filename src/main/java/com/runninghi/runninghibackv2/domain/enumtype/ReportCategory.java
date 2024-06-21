package com.runninghi.runninghibackv2.domain.enumtype;

import lombok.Getter;

@Getter
public enum ReportCategory {

    SPAM(0, "광고, 도배, 스팸"),
    ILLEGAL(1, "불법 정보"),
    ADULT_CONTENT(2, "음란, 청소년 유해"),
    ABUSE(3, "욕설, 비방, 차별, 혐오"),
    PRIVACY(4,"개인 정보 노출, 유포, 거래"),
    OTHER(5, "기타");

    private final int value;
    private final String description;

    ReportCategory(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
