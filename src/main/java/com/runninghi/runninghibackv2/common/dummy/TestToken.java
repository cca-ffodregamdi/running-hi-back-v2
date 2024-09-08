package com.runninghi.runninghibackv2.common.dummy;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.vo.RunDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestToken {

    private final JwtTokenProvider jwtTokenProvider;
    private final TestMemberRepository testMemberRepository;

    @Value("${runninghi.version}")
    private String currentVersion;

    @GetMapping("/test/app-review")
    public ResponseEntity<ApiResult<TestReviewerResponse>> checkVersion(@RequestParam("ver") String version) {  //1.0.1

        String[] versionParts = version.split("\\.");
        String[] currentVersionParts = currentVersion.split("\\.");

        StringBuilder targetVersion = new StringBuilder();
        StringBuilder targetCurrentVersion = new StringBuilder();
        int arrayLength = currentVersionParts.length;
        for ( int i = 0; i < arrayLength; i++ ) {
            targetVersion.append(versionParts[i]);
            targetCurrentVersion.append(currentVersionParts[i]);
        }
//        String minorVersion = versionParts[versionParts.length - 2] + "." + versionParts[versionParts.length - 1];

        String userName = "유저 : 테스트 계정 이름";
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(127.543, 36.9876));

        System.out.println(Integer.parseInt(targetVersion.toString()));
        System.out.println(Integer.parseInt(targetCurrentVersion.toString()));
        System.out.println(Integer.parseInt(targetVersion.toString()) > Integer.parseInt(targetCurrentVersion.toString()));
        if (Integer.parseInt(targetVersion.toString()) > Integer.parseInt(targetCurrentVersion.toString())) {
            Member user = testMemberRepository.findByName(userName)
                    .orElseGet(() -> {
                        Member newUser = Member.builder()
                                .alarmConsent(true)
                                .kakaoId("67890")
                                .name(userName)
                                .nickname(userName)
                                .isActive(true)
                                .role(Role.USER)
                                .runDataVO(new RunDataVO(0.0, 0.0, 2, 1))
                                .geometry(point)
                                .build();
                        testMemberRepository.saveAndFlush(newUser);
                        return newUser;
                    });

            TestReviewerResponse response = createTokens(user);
            return ResponseEntity.ok(ApiResult.success("테스터 토큰 발급 완료", response));
        } else {
            return ResponseEntity.ok(ApiResult.success("테스터가 아닙니다.", new TestReviewerResponse(false, null)));
        }
    }


    @PostMapping("/test/token")
    public ResponseEntity<ApiResult<TestTokenResponse>> getTokens() {
        try {
            String adminName = "관리자 : 테스트 계정 이름";
            String userName = "유저 : 테스트 계정 이름";

            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326); // 4326 is the SRID for WGS84
            Point point = geometryFactory.createPoint(new Coordinate(127.543, 36.9876));

            Member admin = testMemberRepository.findByName(adminName)
                    .orElseGet(() -> {
                        Member newAdmin = Member.builder()
                                .alarmConsent(true)
                                .kakaoId("12345")
                                .name(adminName)
                                .nickname("관리자 : 테스트용 관리자입니다.")
                                .role(Role.ADMIN)
                                .runDataVO(new RunDataVO(0.0,0.0,2,1))
                                .geometry(point)
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
                                .isActive(true)
                                .role(Role.USER)
                                .runDataVO(new RunDataVO(0.0,0.0,2,1))
                                .geometry(point)
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


    private TestReviewerResponse createTokens(Member user) {
        List<Member> members = new ArrayList<>();

        members.add(user);

        AccessTokenInfo userTokenInfo = AccessTokenInfo.from(user);

        String userAccessToken = jwtTokenProvider.createAccessToken(userTokenInfo);

        RefreshTokenInfo userRefreshTokenInfo = RefreshTokenInfo.from(user);

        String userRefreshToken = jwtTokenProvider.createRefreshToken(userRefreshTokenInfo);

        user.updateRefreshToken(userRefreshToken);

        saveAllAndFlushMembers(members);

        TokensAndInfo userTokensAndInfo = TokensAndInfo.from(user, userAccessToken, userRefreshToken);

        return TestReviewerResponse.from(true, userTokensAndInfo);
    }

    private void saveAllAndFlushMembers(List<Member> members) {
        for (Member member : members) {
            testMemberRepository.saveAndFlush(member);
        }
    }

}
