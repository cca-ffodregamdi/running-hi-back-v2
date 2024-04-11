package com.runninghi.runninghibackv2.application.dto.member.response;

public record UpdateMemberInfoResponse(
    String nickname
) {
    public static UpdateMemberInfoResponse from(String nickname) {
        return new UpdateMemberInfoResponse(nickname);
    }
}
