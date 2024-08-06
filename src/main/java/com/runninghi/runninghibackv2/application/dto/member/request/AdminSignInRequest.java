package com.runninghi.runninghibackv2.application.dto.member.request;

public record AdminSignInRequest(
        String account,
        String password
) {
}
