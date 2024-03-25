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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtTokenFilterTests {

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("필터 테스트 : 유효한 액세스 토큰")
    void testDoFilterInternal_ValidAccessToken() throws Exception {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(MockHttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 유효한 경로 설정
        when(request.getRequestURI()).thenReturn("/api/v1");

        // 유효한 엑세스 토큰 설정
        String validAccessToken = "valid_access_token";
        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn(validAccessToken);
        doNothing().when(jwtTokenProvider).validateAccessToken(anyString());

        // 필터 실행
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // FilterChain이 호출되었는지 확인
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("필터 테스트 : 유효하지 않은 액세스 토큰")
    void testDoFilterInternal_InvalidAccessToken() throws IOException, ServletException {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(MockHttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 유효한 경로 설정
        when(request.getRequestURI()).thenReturn("/api/v1");

        // 테스트에 필요한 요청 객체 생성 및 설정
        String invalidAccessToken = "invalid_access_token";
        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn(invalidAccessToken);
        doThrow(InvalidTokenException.class).when(jwtTokenProvider).validateAccessToken(anyString());

        // doFilterInternal 메소드 호출
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // HttpServletResponse의 sendError 메소드가 호출되는지 확인하기 위해 ArgumentCaptor를 생성
        ArgumentCaptor<Integer> statusCodeCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCodeCaptor.capture(), errorMessageCaptor.capture());

        // sendError 메소드 호출 시 전달된 상태 코드와 에러 메시지 전달
        int capturedStatusCode = statusCodeCaptor.getValue();
        String capturedErrorMessage = errorMessageCaptor.getValue();

        // 상태 코드와 에러 메시지 확인
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, capturedStatusCode);
        assertEquals("Invalid Token", capturedErrorMessage);
    }

    @Test
    @DisplayName("필터 테스트 : 만료된 액세스 토큰")
    void testDoFilterInternal_ExpiredAccessToken() {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(MockHttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 만료된 엑세스 토큰을 설정, 예외를 던지도록 설정
        String expiredAccessToken = "expired_access_token";
        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn(expiredAccessToken);
        doThrow(ExpiredJwtException.class).when(jwtTokenProvider).validateAccessToken(anyString());

        // 필터 실행, 예외가 발생하지 않는지 확인
        assertDoesNotThrow(() -> jwtTokenFilter.doFilterInternal(request, response, filterChain));
    }

    @Test
    @DisplayName("필터 테스트 : 만료된 리프레시 토큰")
    void testDoFilterInternal_ExpiredRefreshToken() throws ServletException, IOException {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(MockHttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 유효한 경로 설정
        when(request.getRequestURI()).thenReturn("/api/v1");

        // 만료된 리프레시 토큰을 설정, 예외를 던지도록 설정
        String expiredRefreshToken = "expired_refresh_token";
        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn(null);
        when(jwtTokenProvider.extractRefreshTokenFromRequest(any())).thenReturn(expiredRefreshToken);
        doThrow(ExpiredJwtException.class).when(jwtTokenProvider).validateRefreshToken(anyString());

        // 필터 실행, FilterChain이 호출되었는지 확인
        assertDoesNotThrow(() -> jwtTokenFilter.doFilterInternal(request, response, filterChain));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("필터 테스트 : 서버 에러")
    void testDoFilterInternal_InternalServerError() throws IOException, ServletException {
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(MockHttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // 테스트에 필요한 요청 객체 생성 및 설정
        String accessToken = "valid_access_token";
        when(jwtTokenProvider.extractAccessTokenFromRequest(any())).thenReturn(accessToken);

        // validateAccessToken 메소드가 체크된 예외를 던질 때 처리하기 위해 doAnswer 사용
        doAnswer(invocation -> {
            throw new ServletException("Error");
        }).when(jwtTokenProvider).validateAccessToken(anyString());

        // doFilterInternal 메소드 호출
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // HttpServletResponse의 sendError 메소드가 호출되는지 확인하기 위해 ArgumentCaptor를 생성
        ArgumentCaptor<Integer> statusCodeCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCodeCaptor.capture(), errorMessageCaptor.capture());

        // sendError 메소드 호출 시 전달된 상태 코드와 에러 메시지 전달
        int capturedStatusCode = statusCodeCaptor.getValue();
        String capturedErrorMessage = errorMessageCaptor.getValue();

        // 상태 코드, 에러 메시지 확인
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, capturedStatusCode);
        assertEquals("Internal Server Error", capturedErrorMessage);
    }

}
