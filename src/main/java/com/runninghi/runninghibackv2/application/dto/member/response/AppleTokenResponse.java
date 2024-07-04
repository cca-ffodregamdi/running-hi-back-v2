package com.runninghi.runninghibackv2.application.dto.member.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {
}