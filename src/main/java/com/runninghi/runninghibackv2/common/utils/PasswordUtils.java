package com.runninghi.runninghibackv2.common.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // 비밀번호 암호화
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // 암호화된 비밀번호와 평문 비밀번호를 비교
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // 기본 생성자: 인스턴스 생성을 방지하기 위한 private 생성자
    private PasswordUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
