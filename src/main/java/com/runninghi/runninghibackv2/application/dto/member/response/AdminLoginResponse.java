package com.runninghi.runninghibackv2.application.dto.member.response;

public record AdminLoginResponse(
        String accessToken,
        String refreshToken
) {
    public static AdminLoginResponse from() {
        return new AdminLoginResponse();
    }
}
