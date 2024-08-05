package com.runninghi.runninghibackv2.application.dto.member.request;

public record AdminLoginRequest(
        String account,
        String password
) {
}
