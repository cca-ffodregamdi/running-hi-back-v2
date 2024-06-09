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
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "챌린지 API", description = "챌린지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final MyChallengeService myChallengeService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "챌린지 저장", description = "챌린지를 저장합니다.\n 관리자만 챌린지를 생성할 수 있습니다.")
    @HasAccess
    @PostMapping()
    public ResponseEntity<ApiResult<CreateChallengeResponse>> createChallenge(
            @RequestBody CreateChallengeRequest request) {

        CreateChallengeResponse response = challengeService.createChallenge(request);

        return ResponseEntity.ok(ApiResult.success("챌린지 저장 성공", response));
    }

    @Operation(summary = "챌린지 전체 조회", description = "모든 챌린지를 조회합니다.")
    @GetMapping()
    public ResponseEntity<ApiResult<List<GetChallengeResponse>>> getAllChallenges() {

        List<GetChallengeResponse> response = challengeService.getAllChallenges();

        return ResponseEntity.ok(ApiResult.success("챌린지 전체 조회 성공", response));
    }

    @Operation(summary = "챌린지 상세 조회", description = "챌린지를 상세 정보를 조회합니다.")
    @GetMapping("/{challengeNo}")
    public ResponseEntity<ApiResult<GetChallengeResponse>> getChallengeById(@PathVariable Long challengeNo) {

        GetChallengeResponse response = challengeService.getChallengeById(challengeNo);

        return ResponseEntity.ok(ApiResult.success("챌린지 상세 조회 성공", response));
    }

    @Operation(summary = "챌린지 수정", description = "챌린지 제목, 내용, 카테고리, 목표수치, 시작일자, 종료일자를 수정할 수 있습니다." +
            "\n 관리자만 챌린지를 수정할 수 있습니다")
    @HasAccess
    @PutMapping("/{challengeNo}")
    public ResponseEntity<ApiResult<UpdateChallengeResponse>> updateChallenge(
            @PathVariable Long challengeNo,
            @RequestBody UpdateChallengeRequest request) {

        UpdateChallengeResponse response = challengeService.updateChallenge(challengeNo, request);

        return ResponseEntity.ok(ApiResult.success("챌린지 수정 성공", response));
    }

    @Operation(summary = "챌린지 삭제", description = "챌린지를 삭제합니다. \n 관리자만 챌린지를 삭제할 수 있습니다")
    @HasAccess
    @DeleteMapping("/{challengeNo}")
    public ResponseEntity<ApiResult<DeleteChallengeResponse>> deleteChallenge(@PathVariable Long challengeNo) {

        DeleteChallengeResponse response = challengeService.deleteChallenge(challengeNo);

        return ResponseEntity.ok(ApiResult.success("챌린지 삭제 성공", response));
    }

    @Operation(summary = "나의 챌린지 저장", description = "나의 챌린지를 생성합니다. \n 챌린지 참여하기를 누르면 생성됩니다.")
    @PostMapping("/my-challenge")
    public ResponseEntity<ApiResult<CreateMyChallengeResponse>> createMyChallenge(
            @RequestBody CreateMyChallengeRequest request,
            @RequestHeader(name = "Authorization") String bearerToken) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);

        CreateMyChallengeResponse response = myChallengeService.createMyChallenge(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("나의 챌린지 저장 성공", response));
    }

    @Operation(summary = "나의 챌린지 전체 조회", description = "로그인한 사용자가 참여중인 모든 챌린지를 조회합니다.")
    @GetMapping("/my-challenge")
    public ResponseEntity<ApiResult<List<GetMyChallengeResponse>>> getAllMyChallenges(
            @RequestHeader(name = "Authorization") String bearerToken) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);

        List<GetMyChallengeResponse> response = myChallengeService.getAllMyChallenges(memberNo);

        return ResponseEntity.ok(ApiResult.success("나의 챌린지 전체 조회 성공", response));
    }

    @Operation(summary = "나의 챌린지 상세 조회", description = "로그인한 사용자가 참여중인 챌린지 목록 화면에서 " +
            "선택한 챌린지의 정보를 조회합니다.")
    @GetMapping("/my-challenge/{myChallengeNo}")
    public ResponseEntity<ApiResult<GetMyChallengeResponse>> getMyChallengeById(
            @PathVariable Long myChallengeNo,
            @RequestHeader(name = "Authorization") String bearerToken) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);

        GetMyChallengeResponse response = myChallengeService.getMyChallengeById(memberNo, myChallengeNo);

        return ResponseEntity.ok(ApiResult.success("나의 챌린지 상세 조회 성공", response));
    }
}
