package com.runninghi.runninghibackv2.common.filter;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.exception.custom.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰을 처리하는 필터입니다.
 * 요청에서 JWT 토큰을 추출하고 유효성을 검사한 후 필터 체인을 계속 진행합니다.
 * 만료된 토큰일 경우 리프레시 토큰을 확인하고 재발급합니다.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * HTTP 요청을 필터링하여 JWT 토큰을 처리하는 메서드입니다.
     *
     * @param request  현재 HTTP 요청
     * @param response 현재 HTTP 응답
     * @param filterChain 다음 필터 체인
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException      입출력 예외 발생 시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessToken = jwtTokenProvider.extractAccessToken(request);
            jwtTokenProvider.validateAccessToken(accessToken);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 리프레시 토큰 확인 및 재발급
            String refreshToken = jwtTokenProvider.extractRefreshToken(request);
            jwtTokenProvider.validateRefreshToken(refreshToken);
            String newAccessToken = jwtTokenProvider.renewAccessToken(refreshToken);
            response.setHeader("Authorization", "Bearer " + newAccessToken);

            filterChain.doFilter(request, response);
        } catch (InvalidTokenException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

}