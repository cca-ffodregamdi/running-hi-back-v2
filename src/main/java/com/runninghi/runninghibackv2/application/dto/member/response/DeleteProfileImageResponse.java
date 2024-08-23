package com.runninghi.runninghibackv2.application.dto.member.response;

import com.runninghi.runninghibackv2.domain.entity.Member;

public record DeleteProfileImageResponse(
        Long memberNo,
        String profileImageUrl
) {
    public static DeleteProfileImageResponse of(Member member) {
        return new DeleteProfileImageResponse(member.getMemberNo(), member.getProfileImageUrl());
    }
}
