package com.runninghi.runninghibackv2.reply.domain.service;


import com.runninghi.runninghibackv2.common.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyChecker {

    public boolean memberCheck(Long memberNo, Role role, Long writerNo) {

        boolean isWriter = memberNo.equals(writerNo);
        boolean isAdmin =  Role.ADMIN.equals(role);

        return isWriter || isAdmin;
    }

}
