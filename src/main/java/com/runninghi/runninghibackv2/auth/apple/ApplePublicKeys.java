package com.runninghi.runninghibackv2.auth.apple;

import java.util.List;

import com.runninghi.runninghibackv2.common.exception.custom.AppleOauthException;
import com.runninghi.runninghibackv2.common.exception.custom.ApplePublicKeyNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                .orElseThrow(() ->{
                    log.error("public-key 형태가 잘못되었습니다.");
                    return new ApplePublicKeyNotFoundException();
                });
    }
}