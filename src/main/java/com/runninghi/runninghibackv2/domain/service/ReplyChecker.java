package com.runninghi.runninghibackv2.domain.service;


import com.runninghi.runninghibackv2.domain.enumtype.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ReplyChecker {

    private static final String NO_SPECIAL_SYMBOLS_REGULAR_EXPRESSION = "/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/";


    public boolean memberCheck(Long memberNo, Role role, Long writerNo) {

        boolean isWriter = memberNo.equals(writerNo);
        boolean isAdmin =  Role.ADMIN.equals(role);

        return isWriter || isAdmin;
    }

    public void checkSearchValid(String search) {
        if (!Pattern.matches(NO_SPECIAL_SYMBOLS_REGULAR_EXPRESSION, search)) throw new IllegalArgumentException();

    }
}
