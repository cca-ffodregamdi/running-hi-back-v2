package com.runninghi.runninghibackv2.common.dummy;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/test/token")
    public ResponseEntity<ApiResult<TestTokenResponse>> getTokens() {
        try {
            String adminName = "관리자 : 테스트 계정 이름";
            String userName = "유저 : 테스트 계정 이름";

            Member admin = testMemberRepository.findByName(adminName)
                    .orElseGet(() -> {
                        Member newAdmin = Member.builder()
                                .alarmConsent(true)
                                .kakaoId("12345")
                                .name(adminName)
                                .nickname("관리자 : 테스트용 관리자입니다.")
                                .level(0)
                                .isActive(true)
                                .totalDistance(0)
                                .distanceToNextLevel(10)
                                .role(Role.ADMIN)
                                .build();
                        testMemberRepository.saveAndFlush(newAdmin);
                        return newAdmin;
                    });

            Member user = testMemberRepository.findByName(userName)
                    .orElseGet(() -> {
                        Member newUser = Member.builder()
                                .alarmConsent(true)
                                .kakaoId("67890")
                                .name(userName)
                                .nickname("유저 : 테스트용 유저입니다.")
                                .level(1)
                                .isActive(true)
                                .totalDistance(0)
                                .distanceToNextLevel(10)
                                .role(Role.USER)
                                .build();
                        testMemberRepository.saveAndFlush(newUser);
                        return newUser;
                    });

            TestTokenResponse response = createTokens(admin, user);

            return ResponseEntity.ok(ApiResult.success("test용 유저, 어드민 생성 성공. 토큰 발급 성공", response));
        } catch (IncorrectResultSizeDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.error(HttpStatus.BAD_REQUEST, "이미 테스트 유저가 존재함. 서버 에러."));
        }
    }

    private TestTokenResponse createTokens(Member admin, Member user) {
        List<Member> members = new ArrayList<>();

        members.add(admin);
        members.add(user);

        AccessTokenInfo adminTokenInfo = AccessTokenInfo.from(admin);
        AccessTokenInfo userTokenInfo = AccessTokenInfo.from(user);

        String adminAccessToken = jwtTokenProvider.createAccessToken(adminTokenInfo);
        String userAccessToken = jwtTokenProvider.createAccessToken(userTokenInfo);

        RefreshTokenInfo adminRefreshTokenInfo = RefreshTokenInfo.from(admin);
        RefreshTokenInfo userRefreshTokenInfo = RefreshTokenInfo.from(user);

        String adminRefreshToken = jwtTokenProvider.createRefreshToken(adminRefreshTokenInfo);
        String userRefreshToken = jwtTokenProvider.createRefreshToken(userRefreshTokenInfo);

        admin.updateRefreshToken(adminRefreshToken);
        user.updateRefreshToken(userRefreshToken);

        saveAllAndFlushMembers(members);

        TokensAndInfo adminTokensAndInfo = TokensAndInfo.from(admin, adminAccessToken, adminRefreshToken);
        TokensAndInfo userTokensAndInfo = TokensAndInfo.from(user, userAccessToken, userRefreshToken);

        return TestTokenResponse.from(adminTokensAndInfo, userTokensAndInfo);
    }

    private void saveAllAndFlushMembers(List<Member> members) {
        for (Member member : members) {
            testMemberRepository.saveAndFlush(member);
        }
    }

}
