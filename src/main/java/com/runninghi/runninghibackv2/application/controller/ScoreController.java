package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.service.ScoreService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.application.dto.score.GetRankingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scores")
@Tag(name = "주간 러닝 랭킹 API", description = "달린 거리에 따라 순위를 부여하는 주간 랭킹 API")
public class ScoreController {

    private final ScoreService scoreService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 전체 회원의 러닝 순위를 조회합니다. 달린 거리가 많을수록 높은 순위를 부여합니다.
     * 동일한 값이 있을 경우 동일한 순위를 부여하고 다음 순위를 건너뜁니다.
     * @return 순위값을 포함한 Score 리스트를 반환합니다.
     */
    @Operation(summary = "전체 러닝 랭킹 조회", description = "달린 거리를 기반으로 한 모든 회원의 러닝 순위를 조회합니다.")
    @GetMapping("/ranking")
    public ResponseEntity<ApiResult<List<GetRankingResponse>>> getAllRanking() {

        List<GetRankingResponse> response = scoreService.getAllRanking();

        return ResponseEntity.ok(ApiResult.success("전체 러닝 순위 조회 성공", response));
    }

    /**
     * 특정 회원의 러닝 순위를 조회합니다. 전체 랭킹에서 로그인한 회원의 순위를 조회합니다.
     * @return 순위값을 포함한 Score 데이터를 반환합니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     */
    @Operation(summary = "회원 러닝 랭킹 조회", description = "로그인한 회원의 러닝 순위를 조회합니다.")
    @GetMapping("/ranking/member")
    public ResponseEntity<ApiResult<GetRankingResponse>> getMemberRanking(
            @RequestHeader(name = "Authorization") String bearerToken) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);
        GetRankingResponse response = scoreService.getMemberRanking(memberNo);

        return ResponseEntity.ok(ApiResult.success("회원 러닝 순위 조회 성공", response));
    }
}
