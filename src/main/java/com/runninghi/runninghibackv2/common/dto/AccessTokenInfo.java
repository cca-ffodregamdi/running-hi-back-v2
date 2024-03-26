package com.runninghi.runninghibackv2.common.dto;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;

public record AccessTokenInfo(
        Long memberNo,
        Role role
) {
    public static AccessTokenInfo from(Member member) {
        return new AccessTokenInfo(member.getMemberNo(), member.getRole());
    }
}
