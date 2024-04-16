package com.runninghi.runninghibackv2.domain.enumtype;

import lombok.Getter;

@Getter
public enum ReportCategory {

    SPAM(0, "광고/도배"),
    ILLEGALITY(1, "불법"),
    PORNOGRAPHY(2, "음란물"),
    PROFANITY(3, "욕설/혐오"),
    OTHER(4, "기타");

    private final int value;
    private final String description;

    ReportCategory(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static String getDescriptionFromValue(int value) {
        return switch (value) {
            case 0 -> ReportCategory.SPAM.getDescription();
            case 1 -> ReportCategory.ILLEGALITY.getDescription();
            case 2 -> ReportCategory.PORNOGRAPHY.getDescription();
            case 3 -> ReportCategory.PROFANITY.getDescription();
            case 4 -> ReportCategory.OTHER.getDescription();
            default -> throw new IllegalArgumentException("카테고리 번호가 올바르지 않습니다.");
        };
    }
}
