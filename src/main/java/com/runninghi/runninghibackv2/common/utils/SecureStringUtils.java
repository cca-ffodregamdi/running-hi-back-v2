package com.runninghi.runninghibackv2.common.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureStringUtils {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String buildSecureString(int byteSize) {
        byte[] randomBytes = new byte[byteSize];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().withoutPadding().encodeToString(randomBytes);
    }


}
