package com.runninghi.runninghibackv2.member.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보 변경 응답")
public record UpdateMemberInfoResponse(
        @Schema(description = "변경된 닉네임", example = "뽀둥뽀둥")
        String nickname
) {
    public static UpdateMemberInfoResponse from(String nickname) {
        return new UpdateMemberInfoResponse(nickname);
    }
}
