package com.runninghi.runninghibackv2.domain.enumtype;

import lombok.Getter;

@Getter
public enum ChallengeCategory {

    DISTANCE(0, "거리"),
    SPEED(1, "속도"),
    ATTENDANCE(2, "출석");

    private final int value;
    private final String description;

    ChallengeCategory(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
