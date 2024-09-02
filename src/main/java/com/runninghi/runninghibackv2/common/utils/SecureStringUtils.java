package com.runninghi.runninghibackv2.common.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureStringUtils {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final SecureRandom random = new SecureRandom();

    public static String buildSecureString(int length) {
        if (length < 1) throw new IllegalArgumentException("길이는 최소 1자 이상이어야 합니다.");

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }

}
