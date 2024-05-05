package com.runninghi.runninghibackv2.application.dto.member.response;

import lombok.Getter;

@Getter
public record AppleTokenResponse(
        String accessToken,
        String idToken,
        String refreshToken,
        Long expiresIn,
        String tokenType
) {
}