package com.runninghi.runninghibackv2.application.dto.member.response;

public record AdminLoginResponse(
        Long memberNo
) {
    public static AdminLoginResponse from(Long memberNo) {
        return new AdminLoginResponse(memberNo);
    }
}