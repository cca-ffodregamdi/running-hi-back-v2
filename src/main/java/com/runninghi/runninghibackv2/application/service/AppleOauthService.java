package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.controller.AppleOAuthClient;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;

@Service
@RequiredArgsConstructor
public class AppleOauthService {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;

    private final AppleOAuthClient appleAuthClient;

    public void authenticateWithApple(String code) throws Exception {
        String clientSecret = generateClientSecret("YOUR_TEAM_ID", "YOUR_CLIENT_ID", "YOUR_KEY_ID", "YOUR_PRIVATE_KEY");
        AppleAuthResponse response = appleAuthClient.verifyToken("YOUR_CLIENT_ID", clientSecret, code, "authorization_code", "YOUR_REDIRECT_URI");

        // id_token 파싱
        String idToken = response.getIdToken();

    }

    private String generateClientSecret(String yourTeamId, String yourClientId, String yourKeyId, String yourPrivateKey) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (1000 * 60 * 5)); // 5분 후 만료

        KeyFactory kf = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyContent.getBytes());
        PrivateKey privateKey = kf.generatePrivate(spec);

        return Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setHeaderParam("alg", "ES256")
                .setIssuer(teamId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

}
