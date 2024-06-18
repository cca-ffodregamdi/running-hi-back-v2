package com.runninghi.runninghibackv2.domain.service;


import com.runninghi.runninghibackv2.domain.enumtype.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyChecker {

    private static final String NO_SPECIAL_SYMBOLS_REGULAR_EXPRESSION = "^[ ㄱ-ㅎ가-힣a-zA-Z0-9]*$";


    public boolean memberCheck(Long memberNo, Role role, Long writerNo) {

        boolean isWriter = memberNo.equals(writerNo);
        boolean isAdmin =  Role.ADMIN.equals(role);
        log.info("관리자 혹은 작성자 본인 : " + (isWriter || isAdmin));
        return isWriter || isAdmin;
    }

    public void checkSearchValid(String search) {
        if (!Pattern.matches(NO_SPECIAL_SYMBOLS_REGULAR_EXPRESSION, search)) {
            throw new IllegalArgumentException();
        }

    }
}
