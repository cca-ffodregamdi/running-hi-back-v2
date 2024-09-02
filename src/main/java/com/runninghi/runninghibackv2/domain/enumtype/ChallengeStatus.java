package com.runninghi.runninghibackv2.domain.enumtype;

import lombok.Getter;

@Getter
public enum ChallengeStatus {
    SCHEDULED(0, "예정"),
    IN_PROGRESS(1, "진행중"),
    COMPLETED(2, "종료");

    private final int value;
    private final String description;

    ChallengeStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

}
