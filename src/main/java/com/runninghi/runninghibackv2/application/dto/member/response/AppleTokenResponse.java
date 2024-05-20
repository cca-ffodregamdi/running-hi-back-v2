package com.runninghi.runninghibackv2.application.dto.member.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("id_token") String idToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") Long expiresIn,
        @JsonProperty("token_type") String tokenType
) {
}