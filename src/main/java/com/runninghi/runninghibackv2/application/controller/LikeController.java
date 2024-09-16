package com.runninghi.runninghibackv2.application.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.runninghi.runninghibackv2.application.dto.like.response.LikeResponse;
import com.runninghi.runninghibackv2.application.service.LikeService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/like")
@Tag(name = "좋아요 API", description = "게시글 좋아요 API")
public class LikeController {

    private final JwtTokenProvider jwtTokenProvider;
    private final LikeService likeService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "좋아요 생성",
            description = "특정 게시글을 좋아요합니다. <br /> 사용자 요청으로 '회원 번호'와 '게시글 번호'를 입력 받아 북마크 정보를 저장하고 저장 정보를 반환합니다.",
            responses = @ApiResponse(responseCode = "201", description = "좋아요 생성 성공"))
    public ResponseEntity<ApiResult<LikeResponse>> createLike (@Parameter(description = "사용자 인증을 위한 Bearer 토큰") @RequestHeader("Authorization")String bearerToken,
                                                               @Schema(description = "post 번호", example = "{\"postNo\" : 1}") @RequestBody Map<String, Long> body) {
        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        LikeResponse response = likeService.createLike(accessTokenInfo.memberNo(), body.get("postNo"));

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success("좋아요 생성 성공", response));
    }

    @DeleteMapping(value = "/{postNo}")
    @Operation(
            summary = "좋아요 취소",
            description = "특정 게시글의 좋아요를 취소합니다.",
            responses = @ApiResponse(responseCode = "200", description = "좋아요 취소 성공")
    )
    public ResponseEntity<ApiResult<LikeResponse>> deleteLike(@Parameter(description = "사용자 인증을 위한 Bearer 토큰")
                                                          @RequestHeader("Authorization")String bearerToken,
                                                      @Parameter(description = "좋아요 취소할 특정 게시글 번호")
                                                        @PathVariable(name = "postNo")Long postNo) {

        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        LikeResponse response = likeService.deleteLike(accessTokenInfo.memberNo(), postNo);
        return ResponseEntity.ok().body(ApiResult.success("좋아요 취소 성공", response));
    }


}
