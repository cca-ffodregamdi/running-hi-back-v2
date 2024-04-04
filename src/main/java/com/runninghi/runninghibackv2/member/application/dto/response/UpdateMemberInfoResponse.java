package com.runninghi.runninghibackv2.member.application.dto.response;

public record UpdateMemberInfoResponse(
    String nickname
) {
    public static UpdateMemberInfoResponse from(String nickname) {
        return new UpdateMemberInfoResponse(nickname);
    }
}
