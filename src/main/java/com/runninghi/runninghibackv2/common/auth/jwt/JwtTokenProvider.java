package com.runninghi.runninghibackv2.common.auth.jwt;

import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.common.exception.custom.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long accessExpireMinutes;
    private final long refreshExpireDays;
    private final String issuer;

    /**
     * 지정된 시크릿 키, 액세스 토큰 만료 시간, 리프레시 토큰 만료 시간 및 발행자로 JwtTokenProvider를 구성합니다.
     *
     * @param secretKey          토큰 서명에 사용되는 시크릿 키
     * @param accessExpireMinutes  액세스 토큰의 만료 시간(분)
     * @param refreshExpireDays    리프레시 토큰의 만료 시간(일)
     * @param issuer             토큰 발행자
     */
    public JwtTokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration.access}") long accessExpireMinutes,
            @Value("${jwt.expiration.refresh}") long refreshExpireDays,
            @Value("${jwt.issuer}") String issuer
    ) {
        this.secretKey = secretKey;
        this.accessExpireMinutes = accessExpireMinutes;
        this.refreshExpireDays = refreshExpireDays;
        this.issuer = issuer;
    }

    /**
     * 액세스 토큰을 생성합니다.
     *
     * @param accessTokenInfo 멤버 정보를 담고 있는 객체
     * @return 생성된 액세스 토큰
     */
    public String createAccessToken(AccessTokenInfo accessTokenInfo) {
        LocalDateTime now = LocalDateTime.now();

        return "Bearer " +
                Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setSubject(String.valueOf(accessTokenInfo.memberNo()))
                .claim("role", accessTokenInfo.role())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(now.plusMinutes(accessExpireMinutes).atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }

    /**
     * 리프레시 토큰을 재생성합니다.
     *
     * @param memberJwtInfo 멤버 정보를 담고 있는 객체
     * @return 생성된 리프레시 토큰
     */
    public String createRefreshToken(RefreshTokenInfo refreshTokenInfo) {
        LocalDateTime now = LocalDateTime.now();

        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setSubject(refreshTokenInfo.kakaoId())
                .claim("role", refreshTokenInfo.role())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(now.plusDays(refreshExpireDays).atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuer(issuer)
                .compact();
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 생성합니다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 새로 생성된 액세스 토큰
     * @throws RuntimeException 리프레시 토큰이 잘못된 경우 예외가 발생합니다.
     */
    public String renewAccessToken(String refreshToken) {
        // 리프레시 토큰의 유효성 검증
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        // 리프레시 토큰이 유효한 경우, 새로운 액세스 토큰 생성
        if (claims.getIssuer().equals(issuer)) {
            LocalDateTime now = LocalDateTime.now();

            return Jwts.builder()
                    .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                    .setSubject(claims.getSubject())
                    .claim("role", claims.get("role", String.class))
                    .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .setExpiration(Date.from(now.plusMinutes(accessExpireMinutes).atZone(ZoneId.systemDefault()).toInstant()))
                    .compact();
        } else {
            throw new InvalidTokenException();
        }
    }

    /**
     * HTTP 요청에서 액세스 토큰을 추출합니다.
     *
     * @param request HTTP 요청
     * @return 추출된 액세스 토큰
     * @throws InvalidTokenException Authorization 헤더가 잘못된 경우 예외가 발생합니다.
     */
    public String extractAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            throw new InvalidTokenException();
        }

        return bearerToken;
    }


    /**
     * HTTP 요청에서 리프레시 토큰을 추출합니다.
     *
     * @param request HTTP 요청
     * @return 추출된 리프레시 토큰
     */
    public String extractRefreshTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Refresh-Token");
    }


    /**
     * 액세스 토큰의 유효성을 검사합니다.
     *
     * @param token 검사할 액세스 토큰
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우 예외가 발생합니다.
     */
    public void validateAccessToken(String token) throws InvalidTokenException {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token.substring(7));
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    /**
     * 리프레시 토큰의 유효성을 검사합니다.
     *
     * @param token 검사할 리프레시 토큰
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우 예외가 발생합니다.
     */
    public void validateRefreshToken(String token) throws InvalidTokenException {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    /**
     * 액세스 토큰에서 memberNo를 추출합니다.
     *
     * @param token 데이터를 가져올 액세스 토큰
     * @return 추출된 memberNo
     */
    public Long getMemberNoFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token.substring(7))
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * 액세스 토큰에서 Role을 추출해서 String으로 반환합니다.
     *
     * @param token 데이터를 가져올 액세스 토큰
     * @return 추출된 Role의 name()
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token.substring(7))
                .getBody();

        return claims.get("role", String.class);
    }

    /**
     * Http 요청에서 JwtTokenProvider 내부 메소드들을 이용해 MemberInfo를 추출하는 메소드
     *
     * @param token   Http 요청으로 받은 헤더 내 'Authorization' 토큰
     * @return Key : Values 형태의 MemberInfo("memberNo", "roleName")
     */
    public AccessTokenInfo getMemberInfoByBearerToken (String token) {

        if (token == null) throw new IllegalArgumentException("Invalid Authorization Header");

        String accessToken = token.substring(7); // "Bearer " 이후의 토큰 부분만 추출
        Long memberNo = getMemberNoFromToken(accessToken);
        String roleName = getRoleFromToken(accessToken);

        return new AccessTokenInfo(memberNo, Role.valueOf(roleName));
    }
}
