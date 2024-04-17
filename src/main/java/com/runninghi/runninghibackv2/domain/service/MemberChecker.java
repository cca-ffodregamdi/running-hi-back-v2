package com.runninghi.runninghibackv2.domain.service;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class MemberChecker {

    public void checkNicknameValidation(String nickname) throws BadRequestException {
        if (nickname == null || nickname.length() > 20 || nickname.length() == 0) {
            throw new BadRequestException();
        }
    }
}
