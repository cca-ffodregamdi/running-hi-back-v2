package com.runninghi.runninghibackv2.common.dto;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.domain.entity.Member;

public record RefreshTokenInfo(
        String kakaoId,
        Role role
) {
    public static RefreshTokenInfo from(Member member) {
        return new RefreshTokenInfo(member.getKakaoId(), member.getRole());
    }
}