package com.runninghi.runninghibackv2.common.enumtype;

import lombok.Getter;

@Getter
public enum ReportCategory {

    INPROGRESS(0, "처리중"),
    DELETED(1, "삭제됨"),
    ;

    private final int value;
    private final String description;

    ReportCategory(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static String getDescriptionFromValue(int value) {
        return switch (value) {
            case 0 -> ReportCategory.INPROGRESS.getDescription();
            case 1 -> ReportCategory.DELETED.getDescription();
            default -> throw new IllegalArgumentException("카테고리 번호가 올바르지 않습니다.");
        };
    }

}
