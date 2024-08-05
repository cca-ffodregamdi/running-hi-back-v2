package com.runninghi.runninghibackv2.application.dto.member.request;

public record UpdateCurrentLocationRequest(
        double latitude,
        double longitude
) {
}
