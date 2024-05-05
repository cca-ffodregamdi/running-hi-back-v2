package com.runninghi.runninghibackv2.application.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class ApplePublicKeyFetcher {
    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";

    public PublicKey getApplePublicKey(String kid) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RestTemplate restTemplate = new RestTemplate();
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(APPLE_PUBLIC_KEYS_URL, Map.class);

        if (response != null && response.containsKey("keys")) {
            for (Map<String, Object> key : (Iterable<Map<String, Object>>) response.get("keys")) {
                if (key.get("kid").equals(kid)) {
                    String publicKeyPEM = buildPublicKey((String) key.get("n"), (String) key.get("e"));
                    byte[] encodedKey = Base64.getUrlDecoder().decode(publicKeyPEM);

                    X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedKey);
                    KeyFactory kf = KeyFactory.getInstance("RSA");

                    return kf.generatePublic(spec);
                }
            }
        }

        throw new IllegalArgumentException("Unable to find public key with specified kid");
    }

    private String buildPublicKey(String n, String e) {
        return n + "." + e;
    }
}