package com.runninghi.runninghibackv2.common.dto;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;

public record MemberJwtInfo(
        Long memberNo,
        Role role
) {
    public static MemberJwtInfo from(Member member) {
        return new MemberJwtInfo(member.getMemberNo(), member.getRole());
    }
}
