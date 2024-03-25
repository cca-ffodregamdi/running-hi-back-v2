package com.runninghi.runninghibackv2.member.application.service;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.MemberJwtInfo;
import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.common.exception.custom.KakaoLoginException;
import com.runninghi.runninghibackv2.member.application.dto.response.KakaoProfileResponse;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRefreshTokenRepository;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class KakaoLoginServiceTests {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private MemberRefreshTokenRepository memberRefreshTokenRepository;

    private KakaoProfileResponse kakaoProfileResponse;
    private Member testMember;


    @BeforeEach
    void setUp() {
        kakaoProfileResponse = new KakaoProfileResponse(12345L, new KakaoProfileResponse.KakaoProfile("testNickname"));

        testMember = new Member.MemberBuilder()
                .nickname("testUser1")
                .role(Role.USER)
                .build();

        memberRepository.saveAndFlush(testMember);
    }

    @Test
    void testGetKakaoUri() {
        // given
        String expectedUri = "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoClientId +
                "&redirect_uri=" + kakaoRedirectUri +
                "&response_type=code";

        // when
        String actualUri = kakaoLoginService.getKakaoUri();

        // then
        assertEquals(expectedUri, actualUri);
    }

    @Test
    void whenKakaoOauth_thenLoginOrCreateMember() {
        // given
        String code = "someCode";
        Map<String, String> mockResponse = new HashMap<>();
        mockResponse.put("access_token", "kakaoAccessToken");
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(mockResponse);

        when(restTemplate.postForObject(anyString(), any(), eq(KakaoProfileResponse.class))).thenReturn(kakaoProfileResponse);

        when(memberRepository.findByKakaoId(anyString())).thenReturn(Optional.empty());

        Member mockMember = new Member.MemberBuilder()
                .nickname("testUser1")
                .role(Role.USER)
                .build();
        when(memberRepository.save(any(Member.class))).thenReturn(mockMember);

        when(jwtTokenProvider.createAccessToken(any(MemberJwtInfo.class))).thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(any(MemberJwtInfo.class))).thenReturn("refreshToken");

        // when
        Map<String, String> tokens = kakaoLoginService.kakaoOauth(code);

        // then
        assertNotNull(tokens);
        assertEquals("accessToken", tokens.get("accessToken"));
        assertEquals("refreshToken", tokens.get("refreshToken"));
    }

    @Test
    void whenKakaoOauthAndTokenRequestFails_thenThrowException() {
        // given
        String code = "invalidCode";
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(null);

        // when, then
        assertThrows(KakaoLoginException.class, () -> kakaoLoginService.kakaoOauth(code));
    }

    @Test
    void whenKakaoOauthAndMemberExists_thenLogin() {
        // given
        String code = "someCode";
        setupMockForSuccessfulTokenRequest();

        when(restTemplate.postForObject(anyString(), any(), eq(KakaoProfileResponse.class))).thenReturn(kakaoProfileResponse);

        when(memberRepository.findByKakaoId(anyString())).thenReturn(Optional.of(testMember));

        // when
        Map<String, String> tokens = kakaoLoginService.kakaoOauth(code);

        // then
        assertNotNull(tokens);
        assertFalse(tokens.isEmpty());
    }

    @Test
    void whenCreateMember_thenMemberSavedWithCorrectInfo() {
        // given
        String kakaoId = "12345";
        String nickname = "newMember";
        setupMockForSuccessfulTokenRequest();

        when(restTemplate.postForObject(anyString(), any(), eq(KakaoProfileResponse.class))).thenReturn(kakaoProfileResponse);
        when(memberRepository.findByKakaoId(anyString())).thenReturn(Optional.empty());

        // Mock the behavior of saving a member to return the same member
        when(memberRepository.save(any(Member.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        kakaoLoginService.kakaoOauth("validCode");

        // then
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).saveAndFlush(memberCaptor.capture()); // save() 대신 saveAndFlush()로 변경
        Member savedMember = memberCaptor.getValue();

        assertNotNull(savedMember);
        assertEquals(kakaoId, savedMember.getKakaoId());
        assertEquals(nickname, savedMember.getKakaoName());
    }

    private void setupMockForSuccessfulTokenRequest() {
        Map<String, String> mockResponse = new HashMap<>();
        mockResponse.put("access_token", "kakaoAccessToken");
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(mockResponse);
    }

}





