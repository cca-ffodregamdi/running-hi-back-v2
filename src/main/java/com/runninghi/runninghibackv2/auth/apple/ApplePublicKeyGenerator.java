package com.runninghi.runninghibackv2.auth.apple;

import com.runninghi.runninghibackv2.common.exception.custom.AppleOauthException;
import com.runninghi.runninghibackv2.common.exception.custom.ApplePublicKeyGenerationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
public class ApplePublicKeyGenerator {

    private static final String SIGN_ALGORITHM_HEADER = "alg";
    private static final String KEY_ID_HEADER = "kid";
    private static final int POSITIVE_SIGN_NUMBER = 1;

    public PublicKey generate(Map<String, String> headers, ApplePublicKeys publicKeys) {
        // id_token에서 추출한 alg, kid와 일치하는 alg, kid를 가진 publicKey
        ApplePublicKey applePublicKey = publicKeys.getMatchingKey(
                headers.get(SIGN_ALGORITHM_HEADER),
                headers.get(KEY_ID_HEADER)
        );
        return generatePublicKey(applePublicKey);
    }

    // publicKey를 통해 RSAPublicKey 생성 & JWS E256 Signature 검증
    private PublicKey generatePublicKey(ApplePublicKey applePublicKey) {
        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.n());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.e());

        // publicKey의 n, e 값으로 RSAPublicKeySpec 생성
        BigInteger n = new BigInteger(POSITIVE_SIGN_NUMBER, nBytes);
        BigInteger e = new BigInteger(POSITIVE_SIGN_NUMBER, eBytes);
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);

        try {
            // publicKey의 kty 값으로 KeyFactory 생성
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.kty());
            // 생성한 KeyFactory와 PublicKeySpec으로 RSAPublicKey 생성
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("RSAPublicKey 생성 중 오류가 발생했습니다.");
            throw new ApplePublicKeyGenerationException();
        }
    }
}