package com.runninghi.runninghibackv2.application.dto.member.request;

import lombok.Getter;

@Getter
public record UpdateCurrentLocationRequest(
        double x,
        double y
) {
}
