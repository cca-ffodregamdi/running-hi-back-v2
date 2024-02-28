package com.runninghi.runninghibackv2.feedback.domain.aggregate.entity;

public enum FeedbackCategory {
    INQUIRY(0, "문의 사항"),
    PROPOSAL(1, "개선 사항"),
    WEBERROR(2, "웹페이지 오류"),
    ROUTEERROR(3, "경로 오류"),
    POSTERROR(4, "게시글 내용 오류");

    private final int value;
    private final String description;

    FeedbackCategory(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionFromValue(int value) {
        return switch (value) {
            case 0 -> FeedbackCategory.INQUIRY.getDescription();
            case 1 -> FeedbackCategory.PROPOSAL.getDescription();
            case 2 -> FeedbackCategory.WEBERROR.getDescription();
            case 3 -> FeedbackCategory.ROUTEERROR.getDescription();
            case 4 -> FeedbackCategory.POSTERROR.getDescription();
            default -> throw new IllegalArgumentException("카테고리 번호가 올바르지 않습니다.");
        };
    }
}

