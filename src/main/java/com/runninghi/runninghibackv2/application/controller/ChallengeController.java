package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.memberchallenge.request.CreateMyChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.request.UpdateChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.response.*;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.CreateMyChallengeResponse;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.GetMyChallengeResponse;
import com.runninghi.runninghibackv2.application.service.ChallengeService;
import com.runninghi.runninghibackv2.application.dto.challenge.request.CreateChallengeRequest;
import com.runninghi.runninghibackv2.application.service.MyChallengeService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final MyChallengeService myChallengeService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping()
    public ResponseEntity<ApiResult<CreateChallengeResponse>> createChallenge(
            @RequestBody CreateChallengeRequest request) {

        CreateChallengeResponse response = challengeService.createChallenge(request);

        return ResponseEntity.ok(ApiResult.success("챌린지 저장 성공", response));
    }

    @GetMapping()
    public ResponseEntity<ApiResult<List<GetChallengeResponse>>> getAllChallenges() {

        List<GetChallengeResponse> response = challengeService.getAllChallenges();

        return ResponseEntity.ok(ApiResult.success("챌린지 전체 조회 성공", response));
    }

    @GetMapping("/{challengeNo}")
    public ResponseEntity<ApiResult<GetChallengeResponse>> getChallengeById(@PathVariable Long challengeNo) {

        GetChallengeResponse response = challengeService.getChallengeById(challengeNo);

        return ResponseEntity.ok(ApiResult.success("챌린지 상세 조회 성공", response));
    }

    @PutMapping("/{challengeNo}")
    public ResponseEntity<ApiResult<UpdateChallengeResponse>> updateChallenge(
            @PathVariable Long challengeNo,
            @RequestBody UpdateChallengeRequest request) {

        UpdateChallengeResponse response = challengeService.updateChallenge(challengeNo, request);

        return ResponseEntity.ok(ApiResult.success("챌린지 수정 성공", response));
    }

    @DeleteMapping("/{challengeNo}")
    public ResponseEntity<ApiResult<DeleteChallengeResponse>> deleteChallenge(@PathVariable Long challengeNo) {

        DeleteChallengeResponse response = challengeService.deleteChallenge(challengeNo);

        return ResponseEntity.ok(ApiResult.success("챌린지 삭제 성공", response));
    }

    @PostMapping("/my-challenge")
    public ResponseEntity<ApiResult<CreateMyChallengeResponse>> createMyChallenge(
            @RequestBody CreateMyChallengeRequest request,
            @RequestHeader(name = "Authorization") String bearerToken) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);

        CreateMyChallengeResponse response = myChallengeService.createMyChallenge(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("나의 챌린지 저장 성공", response));
    }

    @GetMapping("/my-challenge")
    public ResponseEntity<ApiResult<List<GetMyChallengeResponse>>> getAllMyChallenges(
            @RequestHeader(name = "Authorization") String bearerToken) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);

        List<GetMyChallengeResponse> response = myChallengeService.getAllMyChallenges(memberNo);

        return ResponseEntity.ok(ApiResult.success("나의 챌린지 전체 조회 성공", response));
    }

    @GetMapping("/my-challenge/{myChallengeNo}")
    public ResponseEntity<ApiResult<GetMyChallengeResponse>> getMyChallengeById(
            @PathVariable Long myChallengeNo,
            @RequestHeader(name = "Authorization") String bearerToken) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);

        GetMyChallengeResponse response = myChallengeService.getMyChallengeById(memberNo, myChallengeNo);

        return ResponseEntity.ok(ApiResult.success("나의 챌린지 상세 조회 성공", response));
    }
}
