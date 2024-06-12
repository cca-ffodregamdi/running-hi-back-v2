package com.runninghi.runninghibackv2.auth.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runninghi.runninghibackv2.common.exception.custom.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleTokenParser {

    private static final String ID_TOKEN_SEPARATOR = "\\.";
    private static final int HEADER_INDEX = 0;

    private final ObjectMapper objectMapper;

    public Map<String, String> parseHeader(String idToken) {
        try {
            final String encodedHeader = idToken.split(ID_TOKEN_SEPARATOR)[HEADER_INDEX];
            final String decodedHeader = new String(Base64.getUrlDecoder().decode(encodedHeader));

            return objectMapper.readValue(decodedHeader, Map.class);

        } catch (JsonMappingException e) {
            throw new InvalidTokenException("Parsing Header Error : 토큰의 헤더를 매핑하는 데 실패했습니다");
        } catch (JsonProcessingException e) {
            throw new InvalidTokenException("Parsing Header Error : 토큰 처리 중 오류가 발생했습니다");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidTokenException("Parsing Header Error : 유효하지 않은 토큰 형식입니다.");
        }
    }

    // claims를 추출하여 사용자 정보 가져오기
    public Claims extractClaims(String idToken, PublicKey publicKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("Extract Claims Error : 지원되지 않는 JWT 형식입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Extract Claims Error : 유효하지 않은 값입니다.");
        } catch (JwtException e) {
            throw new JwtException("Extract Claims Error : JWT 처리 중 오류가 발생했습니다.");
        }
    }
}
