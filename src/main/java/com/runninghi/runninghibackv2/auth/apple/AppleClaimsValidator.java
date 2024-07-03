package com.runninghi.runninghibackv2.auth.apple;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppleClaimsValidator {

    private final String iss;
    private final String clientId;

    public AppleClaimsValidator(
            @Value("${apple.iss}") String iss,
            @Value("${apple.client-id}") String clientId
    ) {
        this.iss = iss;
        this.clientId = clientId;
    }

    public boolean isValid(Claims claims) {
        // exp, iss, aud 검증
        Date expiration = claims.getExpiration();
        Date currentDate = new Date();

        return currentDate.before(expiration) &&
                claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId);
    }
}