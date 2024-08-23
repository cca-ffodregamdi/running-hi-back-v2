package com.runninghi.runninghibackv2.application.dto.member.response;

import com.runninghi.runninghibackv2.domain.entity.Member;

public record UpdateProfileImageResponse(
        Long memberNo,
        String profileImageUrl
) {
    public static UpdateProfileImageResponse of (Member member) {
        return new UpdateProfileImageResponse(member.getMemberNo(), member.getProfileImageUrl());
    }
}
