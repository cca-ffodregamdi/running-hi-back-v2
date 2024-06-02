package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.challenge.request.UpdateChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.response.GetChallengeResponse;
import com.runninghi.runninghibackv2.application.dto.challenge.response.UpdateChallengeResponse;
import com.runninghi.runninghibackv2.application.service.ChallengeService;
import com.runninghi.runninghibackv2.application.dto.challenge.request.CreateChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.response.CreateChallengeResponse;
import com.runninghi.runninghibackv2.application.dto.challenge.response.DeleteChallengeResponse;
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
}
