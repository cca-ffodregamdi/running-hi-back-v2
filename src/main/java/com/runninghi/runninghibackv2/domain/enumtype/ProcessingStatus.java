package com.runninghi.runninghibackv2.domain.enumtype;

import lombok.Getter;

@Getter
public enum ProcessingStatus {

    INPROGRESS(0, "처리중"),
    ACCEPTED(1, "수락됨"),
    REJECTED(2, "거절됨");

    private final int value;
    private final String description;

    ProcessingStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static String getDescriptionFromValue(int value) {
        return switch (value) {
            case 0 -> ProcessingStatus.INPROGRESS.getDescription();
            case 1 -> ProcessingStatus.ACCEPTED.getDescription();
            case 2 -> ProcessingStatus.REJECTED.getDescription();
            default -> throw new IllegalArgumentException("처리상태 번호가 올바르지 않습니다.");
        };
    }

    public static ProcessingStatus fromValue (int value) {
        for (ProcessingStatus status : ProcessingStatus.values()) {
            if (status.value == value) return status;
        }
        throw new IllegalArgumentException("신고 처리 상태 번호가 올바르지 않습니다.");
    }
}
