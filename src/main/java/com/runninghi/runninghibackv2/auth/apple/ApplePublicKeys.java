package com.runninghi.runninghibackv2.auth.apple;

import java.util.List;

import com.runninghi.runninghibackv2.common.exception.custom.AppleOauthException;

public record ApplePublicKeys(
        List<ApplePublicKey> keys

) {
    public ApplePublicKeys {
        keys = List.copyOf(keys);
    }

    public ApplePublicKey getMatchingKey(final String alg, final String kid) {
        return keys.stream()
                .filter(key -> key.isSameAlg(alg) && key.isSameKid(kid))
                .findFirst()
                .orElseThrow(() -> new AppleOauthException("public-key 형태가 잘못되었습니다."));
    }
}