package com.runninghi.runninghibackv2.common.filter;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.exception.custom.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
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
     * HTTP 요청을 필터링하여 JWT 토큰을 처리합니다.
     *
     * @param request  현재 HTTP 요청
     * @param response 현재 HTTP 응답
     * @param filterChain 다음 필터 체인
     * @throws IOException      입출력 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {
        try {
            String accessToken = jwtTokenProvider.extractAccessToken(request);
            jwtTokenProvider.validateAccessToken(accessToken);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleExpiredAccessToken(request, response, filterChain);
        } catch (InvalidTokenException e) {
            handleInvalidToken(response);
        } catch (Exception e) {
            handleInternalServerError(response);
        }
    }

    /**
     * 만료된 액세스 토큰을 처리합니다.
     * 리프레시 토큰을 추출하고 유효성을 검사한 후, 새로운 액세스 토큰을 발급하여 응답의 헤더에 설정합니다.
     * 필터 체인을 계속 진행합니다.
     *
     * @param request     현재 HTTP 요청
     * @param response    현재 HTTP 응답
     * @param filterChain 다음 필터 체인
     * @throws IOException      입출력 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시
     */
    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String refreshToken = jwtTokenProvider.extractRefreshToken(request);
        try {
            jwtTokenProvider.validateRefreshToken(refreshToken);
            String newAccessToken = jwtTokenProvider.renewAccessToken(refreshToken);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException ex) {
            handleUnauthorizedResponse(response);
        }
    }

    /**
     * 유효하지 않은 토큰을 처리합니다.
     * HTTP 응답에 401 상태 코드를 설정하고 메시지를 전송합니다.
     *
     * @param response 현재 HTTP 응답
     * @throws IOException 입출력 예외 발생 시
     */
    private void handleInvalidToken(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
    }

    /**
     * 내부 서버 오류를 처리합니다.
     * HTTP 응답에 500 상태 코드를 설정하고 메시지를 전송합니다.
     *
     * @param response 현재 HTTP 응답
     * @throws IOException 입출력 예외 발생 시
     */
    private void handleInternalServerError(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    /**
     * 권한이 없는 응답을 처리합니다.
     * HTTP 응답에 401 상태 코드를 설정하고 메시지를 전송합니다.
     *
     * @param response 현재 HTTP 응답
     * @throws IOException 입출력 예외 발생 시
     */
    private void handleUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token expired. Please log in again.");
    }

}