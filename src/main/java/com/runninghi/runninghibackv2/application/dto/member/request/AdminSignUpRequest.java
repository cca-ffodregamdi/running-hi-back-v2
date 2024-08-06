package com.runninghi.runninghibackv2.application.dto.member.request;

public record AdminSignUpRequest(
        String account,
        String password,
        String invitationCode
) {
}
