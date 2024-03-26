package com.runninghi.runninghibackv2.auto.jwt;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTests {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String testAccessToken;
    private String testRefreshToken;

    @BeforeEach
    public void setUp() {
        // 테스트에 사용될 JwtTokenProvider 인스턴스 생성
        String secretKey = "test_secret_key_12345_12345_12345_12345";
        String kakaoId = "13245";
        long accessExpireMinutes = 30;
        long refreshExpireDays = 7;
        String issuer = "test_issuer";
        jwtTokenProvider = new JwtTokenProvider(secretKey, accessExpireMinutes, refreshExpireDays, issuer);

        AccessTokenInfo accessTokenInfo = new AccessTokenInfo(1L, Role.USER);
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo(kakaoId, Role.USER);
        testAccessToken = jwtTokenProvider.createAccessToken(accessTokenInfo);
        testRefreshToken = jwtTokenProvider.createRefreshToken(refreshTokenInfo);
    }

    @Test
    @DisplayName("액세스 토큰 생성 테스트")
    void testCreateAccessToken() {
        // 테스트에 사용될 MemberJwtInfo 객체 생성
        AccessTokenInfo accessTokenInfo = new AccessTokenInfo(1L, Role.USER);

        // 액세스 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(accessTokenInfo);

        // 액세스 토큰이 null이 아닌지 확인
        assertNotNull(accessToken);
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    void testCreateRefreshToken() {
        // 테스트에 사용될 MemberJwtInfo 객체 생성
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo("12345", Role.USER);

        // 리프레시 토큰 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(refreshTokenInfo);

        // 리프레시 토큰이 null이 아닌지 확인
        assertNotNull(refreshToken);
    }


    @Test
    @DisplayName("액세스 토큰 유효성 검사 테스트")
    void testValidateAccessToken() {
        // 액세스 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(new AccessTokenInfo(1L, Role.USER));

        // 액세스 토큰 유효성 검사
        assertDoesNotThrow(() -> jwtTokenProvider.validateAccessToken(accessToken));
    }

    @Test
    @DisplayName("리프레시 토큰 유효성 검사 테스트")
    void testValidateRefreshToken() {
        // 리프레시 토큰 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(new RefreshTokenInfo("12345", Role.USER));

        // 리프레시 토큰 유효성 검사
        assertDoesNotThrow(() -> jwtTokenProvider.validateRefreshToken(refreshToken));
    }

    @Test
    @DisplayName("액세스 토큰 갱신 테스트")
    void testRenewAccessToken() {
        // 리프레시 토큰 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(new RefreshTokenInfo("12345", Role.USER));

        // 액세스 토큰 갱신
        String renewedAccessToken = jwtTokenProvider.renewAccessToken(refreshToken);

        // 갱신된 액세스 토큰이 null이 아닌지 확인
        assertNotNull(renewedAccessToken);
    }

    @Test
    @DisplayName("액세스 토큰에서 memberNo 추출 테스트")
    void testGetMemberNoFromToken() {
        // 액세스 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(new AccessTokenInfo(1L, Role.USER));

        // memberNo 추출
        Long memberNo = jwtTokenProvider.getMemberNoFromToken(accessToken);

        // 추출된 memberNo가 null이 아닌지 확인
        assertNotNull(memberNo);
    }

    @Test
    @DisplayName("Request에서 토큰 추출 테스트")
    void testExtractToken() {
        // 가짜 HttpServletRequest 객체 생성
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", testAccessToken);
        mockHttpServletRequest.addHeader("Refresh-Token", testRefreshToken);

        // 액세스 토큰 추출
        String extractedAccessToken = jwtTokenProvider.extractAccessTokenFromRequest(mockHttpServletRequest);

        // 리프레시 토큰 추출
        String extractedRefreshToken = jwtTokenProvider.extractRefreshTokenFromRequest(mockHttpServletRequest);

        // 추출된 토큰이 null이 아닌지 확인
        assertNotNull(extractedAccessToken);
        assertNotNull(extractedRefreshToken);
    }


    @Test
    @DisplayName("액세스 토큰에서 Role 추출 테스트")
    void testGetRoleFromToken() {
        // Role 추출
        String role = jwtTokenProvider.getRoleFromToken(testAccessToken);

        // 추출된 Role이 null이 아닌지 확인
        assertEquals(Role.USER.name(), role);
    }
}
