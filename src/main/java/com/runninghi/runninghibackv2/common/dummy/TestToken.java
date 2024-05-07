package com.runninghi.runninghibackv2.common.dummy;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestToken {

    private final JwtTokenProvider jwtTokenProvider;
    private final TestMemberRepository testMemberRepository;

    @PostMapping("/test-token")
    public ResponseEntity<ApiResult<TestTokenResponse>> getTokens() {

        List<Member> members = new ArrayList<>();

        Member member1 = Member.builder()
                .alarmConsent(true)
                .kakaoName("관리자 : 카카오 이름")
                .nickname("관리자 : 테스트용 관리자입니다.")
                .level(0)
                .isActive(true)
                .totalDistance(0)
                .distanceToNextLevel(10)
                .role(Role.ADMIN)
                .build();
        members.add(member1);

        Member member2 = Member.builder()
                .alarmConsent(true)
                .kakaoName("유저 : 카카오 이름")
                .nickname("유저 : 테스트용 유저입니다.")
                .level(1)
                .isActive(true)
                .totalDistance(0)
                .distanceToNextLevel(10)
                .role(Role.USER)
                .build();
        members.add(member2);

        saveAllAndFlushMembers(members);

        Member admin = testMemberRepository.findByNickname(member1.getNickname());
        Member user = testMemberRepository.findByNickname(member2.getNickname());

        AccessTokenInfo adminTokenInfo = AccessTokenInfo.from(admin);
        AccessTokenInfo userTokenInfo = AccessTokenInfo.from(user);

        String adminAccessToken = jwtTokenProvider.createAccessToken(adminTokenInfo);
        String userAccessToken = jwtTokenProvider.createAccessToken(userTokenInfo);

        RefreshTokenInfo adminRefreshTokenInfo = RefreshTokenInfo.from(admin);
        RefreshTokenInfo userRefreshTokenInfo = RefreshTokenInfo.from(user);

        String adminRefreshToken = jwtTokenProvider.createRefreshToken(adminRefreshTokenInfo);
        String userRefreshToken = jwtTokenProvider.createRefreshToken(userRefreshTokenInfo);

        member1.updateRefreshToken(adminRefreshToken);
        member2.updateRefreshToken(userRefreshToken);

        saveAllAndFlushMembers(members);

        TokensAndInfo adminTokensAndInfo = TokensAndInfo.from(admin, adminAccessToken, adminRefreshToken);
        TokensAndInfo userTokensAndInfo = TokensAndInfo.from(user, userAccessToken, userRefreshToken);

        TestTokenResponse response = TestTokenResponse.from(adminTokensAndInfo, userTokensAndInfo);

        return ResponseEntity.ok(ApiResult.success("test용 유저, 어드민 생성 성공. 토큰 발급 성공", response));
    }

    private void saveAllAndFlushMembers(List<Member> members) {
        for (Member member : members) {
            testMemberRepository.saveAndFlush(member);
        }
    }

}
