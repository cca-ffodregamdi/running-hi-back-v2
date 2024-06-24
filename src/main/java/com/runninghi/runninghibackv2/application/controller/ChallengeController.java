package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.memberchallenge.request.CreateMyChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.request.UpdateChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.response.*;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.CreateMyChallengeResponse;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.GetAllMyChallengeResponse;
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

    /**
     * 챌린지를 생성합니다.
     * @return 저장된 Challenge 데이터를 반환합니다.
     * @apiNote 관리자만 권한이 있습니다.
     */
    @Operation(summary = "챌린지 저장", description = "챌린지를 저장합니다.\n 관리자만 챌린지를 생성할 수 있습니다.")
    @HasAccess
    @PostMapping()
    public ResponseEntity<ApiResult<CreateChallengeResponse>> createChallenge(
            @RequestBody CreateChallengeRequest request) {

        CreateChallengeResponse response = challengeService.createChallenge(request);

        return ResponseEntity.ok(ApiResult.success("챌린지 저장 성공", response));
    }

    /**
     * 활성화 상태 여부 값으로 챌린지를 조회합니다.
     * @param status true이면 현재 진행중인 챌린지, false인 경우 종료된 챌린지가 조회됩니다.
     * @return Challenge 리스트를 반환합니다.
     */
    @Operation(summary = "활성화 상태 여부에 따른 챌린지 전체 조회", description = "status가 true이면 현재 진행중인 챌린지 \n" +
            "status가 false인 경우 종료된 챌린지가 조회됩니다.")
    @GetMapping("/status")
    public ResponseEntity<ApiResult<List<GetAllChallengeResponse>>> getAllChallengesByStatus(
            @RequestParam(name = "status") boolean status) {

        List<GetAllChallengeResponse> response = challengeService.getAllChallengesByStatus(status);

        return ResponseEntity.ok(ApiResult.success("챌린지 전체 조회 성공", response));
    }

    /**
     * 랭킹을 포함한 챌린지 상세 정보를 조회합니다.
     * 랭킹 조회 시 전체 회원의 러닝 순위를 조회합니다. 기록이 높을수록 높은 순위를 부여합니다.
     * 동일한 값이 있을 경우 동일한 순위를 부여하고 다음 순위를 건너뜁니다.
     * 특정 회원의 러닝 순위를 조회합니다. 전체 랭킹에서 로그인한 회원의 순위를 조회합니다.
     * @return 랭킹을 포함한 Challenge 데이터를 반환합니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     */
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

    /**
     * 회원이 참여한 챌린지 데이터입니다.
     * 활성화 상태 여부 값으로 회원의 챌린지를 조회합니다.
     * @param status true이면 현재 진행중인 챌린지, false인 경우 종료된 챌린지가 조회됩니다.
     * @return MemberChallenge 리스트를 반환합니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     */
    @Operation(summary = "활성화 여부에 따른 나의 챌린지 전체 조회",
            description = "status가 true이면 로그인한 사용자가 참여한 모든 진행중인 챌린지,\nfalse인 경우 종료된 챌린지가 조회됩니다.")
    @GetMapping("/my-challenge/status")
    public ResponseEntity<ApiResult<List<GetAllMyChallengeResponse>>> getAllMyChallengesByStatus(
            @RequestParam(name = "status") boolean status,
            @RequestHeader(name = "Authorization") String bearerToken) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(bearerToken);

        List<GetAllMyChallengeResponse> response = myChallengeService.getAllMyChallengesByStatus(memberNo, status);

        return ResponseEntity.ok(ApiResult.success("나의 챌린지 전체 조회 성공", response));
    }

    /**
     * 회원이 참여한 챌린지 데이터입니다.
     * 랭킹을 포함한 나의 챌린지 상세 정보를 조회합니다.
     * 전체 회원 랭킹과 로그인한 회원의 랭킹을 조회합니다. 기록이 높을수록 높은 순위를 부여합니다.
     * 동일한 값이 있을 경우 동일한 순위를 부여하고 다음 순위를 건너뜁니다.
     * @return 랭킹을 포함한 Challenge 데이터를 반환합니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     */
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
