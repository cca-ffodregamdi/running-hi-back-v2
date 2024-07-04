package com.runninghi.runninghibackv2.application.dto.member.request;


public record AppleLoginRequest(
        String identityToken,
        String authorizationCode
) {
}
