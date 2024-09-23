package com.runninghi.runninghibackv2.MotherObject;

import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.vo.RunDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.Role;

public final class MemberMother {

    public static Member createUserMember(String nickname) {
        return Member.builder()
                .nickname(nickname)
                .role(Role.USER)
                .runDataVO(new RunDataVO(1000,0.0,2,1))
                .build();
    }

    public static Member createAdminMember(String nickname) {
        return Member.builder()
                .nickname(nickname)
                .role(Role.ADMIN)
                .build();
    }

}
