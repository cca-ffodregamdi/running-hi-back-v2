package com.runninghi.runninghibackv2.common.filter;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.exception.custom.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@SpringBootTest
class JwtTokenFilterTests {

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그인 경로는 필터링하지 않음 : success")
    void testShouldNotFilter_LoginPath() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/login/kakao");

        assert jwtTokenFilter.shouldNotFilter(request);
    }

    @Test
    @DisplayName("로그인 경로가 아닌 경우 필터링 : success")
    void testShouldNotFilter_NonLoginPath() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/resources");

        assert !jwtTokenFilter.shouldNotFilter(request);
    }

    @Test
    @DisplayName("유효한 액세스 토큰 : success")
    void testDoFilterInternal_ValidAccessToken() throws Exception {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(MockHttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 유효한 경로 & 메소드 실행 시 반환값과 동작 설정
        when(request.getRequestURI()).thenReturn("/api/v1");
        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn("access_token");
        doNothing().when(jwtTokenProvider).validateAccessToken(anyString());

        // 필터 실행
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // FilterChain이 호출되었는지 확인
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("만료된 리프레시 토큰 : error")
    void testDoFilterInternal_ExpiredRefreshToken() throws ServletException, IOException {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(MockHttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 유효한 경로 & 메소드 실행 시 반환값과 동작 설정
        when(request.getRequestURI()).thenReturn("/api/v1");
        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn("expired_access_token");
        doThrow(ExpiredJwtException.class).when(jwtTokenProvider).validateAccessToken(anyString());
        when(jwtTokenProvider.extractRefreshTokenFromRequest(any())).thenReturn("expired_refresh_token");
        doThrow(InvalidTokenException.class).when(jwtTokenProvider).validateRefreshToken(anyString());

        // PrintWriter를 mock으로 생성하여 반환하도록 설정
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // 필터 실행
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // 호출 횟수 확인
        verify(jwtTokenProvider, times(1)).validateAccessToken(anyString());
        verify(jwtTokenProvider, times(1)).validateRefreshToken(anyString());
        verify(response.getWriter(), times(1)).write(anyString());
    }

    @Test
    @DisplayName("유효하지 않은 액세스 토큰 : error")
    void testDoFilterInternal_InvalidAccessToken() throws IOException, ServletException {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 유효한 경로 & 메소드 실행 시 반환값과 동작 설정
        when(request.getRequestURI()).thenReturn("/api/v1");
        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn("invalid_access_token");
        doThrow(new InvalidTokenException()).when(jwtTokenProvider).validateAccessToken(anyString());

        // PrintWriter를 mock으로 생성하여 반환하도록 설정
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // 필터 실행
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // 호출 횟수 확인
        verify(jwtTokenProvider, times(1)).validateAccessToken(anyString());
        verify(response.getWriter(), times(1)).write(anyString());
    }

    @Test
    @DisplayName("만료된 액세스 토큰 : success")
    void testDoFilterInternal_ExpiredAccessToken() throws ServletException, IOException {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 유효한 경로 설정
        when(request.getRequestURI()).thenReturn("/api/v1");

        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn("expired_access_token");
        when(jwtTokenProvider.extractRefreshTokenFromRequest(any())).thenReturn("refresh_token");
        when(jwtTokenProvider.renewAccessToken(any())).thenReturn("new_access_token");
        doThrow(ExpiredJwtException.class).when(jwtTokenProvider).validateAccessToken(anyString());

        // PrintWriter를 mock으로 생성하여 반환하도록 설정
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // 필터 실행
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // 호출 횟수 확인
        verify(jwtTokenProvider, times(1)).validateAccessToken(anyString());
        verify(jwtTokenProvider, times(1)).validateRefreshToken(anyString());
        verify(response.getWriter(), times(0)).write(anyString());
    }

}