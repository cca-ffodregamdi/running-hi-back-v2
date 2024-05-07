package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.bookmark.request.CreateBookmarkRequest;
import com.runninghi.runninghibackv2.application.dto.bookmark.response.BookmarkedPostListResponse;
import com.runninghi.runninghibackv2.application.dto.bookmark.response.CreateBookmarkResponse;
import com.runninghi.runninghibackv2.application.service.BookmarkService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/bookmark")
@Tag(name = "북마크 API", description = "게시글 북마크 API")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final JwtTokenProvider jwtTokenProvider;

    private final static String GET_RESPONSE_MESSAGE = "북마크된 게시물 리스트 조회 성공";
    private final static String POST_RESPONSE_MESSAGE = "북마크 생성 성공";
    private final static String DELETE_RESPONSE_MESSAGE = "북마크 취소 성공";

    /**
     * 북마크 탭 클릭 시 요청한 사용자의 북마크된 Post들을 응답해주는 메소드입니다.
     * -> 만약 다른 회원의 북마크 리스트를 보게 한다면 리팩토링 필요
     * @param bearerToken 사용자 인증을 위한 Bearer 토큰입니다. 헤더에 포함되어야 합니다.
     * @return Bookmark 테이블에서 조회된 Posts 반환합니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     */
    @GetMapping()
    @Operation(
            summary = "북마크된 게시물 리스트 조회",
            description = "사용자의 북마크된 게시글들을 조회합니다. <br /> 사용자의 북마크 게시물의 리스트를 반환합니다. ",
            parameters = @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
            responses = @ApiResponse(responseCode = "200", description = GET_RESPONSE_MESSAGE)
    )
    public ResponseEntity<ApiResult<List<BookmarkedPostListResponse>>> getBookmarkedPostList(@RequestHeader(name = "Authorization") String bearerToken) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        List<BookmarkedPostListResponse> bookmarkedPostList = bookmarkService.getBookmarkedPostList(memberInfo.memberNo());

        return ResponseEntity.ok().body(ApiResult.success(GET_RESPONSE_MESSAGE, bookmarkedPostList));
    }

    @PostMapping()
    @Operation(
            summary = "북마크 생성",
            description = "특정 게시물을 북마크합니다. <br /> 사용자 요청으로 '사용자 번호'와 '게시글 번호'를 입력 받아 북마크 정보를 저장하고 저장 정보를 반환합니다.",
            parameters = @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
            responses = @ApiResponse(responseCode = "201", description = POST_RESPONSE_MESSAGE)
    )
    public ResponseEntity<ApiResult<CreateBookmarkResponse>> createBookmark (@RequestHeader("Authorization") String bearerToken,
                                                                             @RequestBody(required = true) @Schema(description = "post key 값", example = "{\"postNo\" : 1}") Map<String, Long> body) {

        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        CreateBookmarkRequest request = CreateBookmarkRequest.of(accessTokenInfo.memberNo(), body.get("postNo"));
        CreateBookmarkResponse response = bookmarkService.createBookmark(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(POST_RESPONSE_MESSAGE, response));
    }

    @DeleteMapping(value = "/{postNo}")
    @Operation(
            summary = "북마크 취소",
            description = "특정 게시물의 북마크를 취소합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "postNo", description = "북마크 취소할 특정 게시물 번호", required = true)
            },
            responses = @ApiResponse(responseCode = "204", description = DELETE_RESPONSE_MESSAGE)

    )
    public ResponseEntity<ApiResult> deleteBookmark (@RequestHeader("Authorization") String bearerToken,
                                                     @PathVariable(name = "postNo") Long postNo) {

        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        bookmarkService.deleteBookmark(accessTokenInfo.memberNo(), postNo);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResult.success(DELETE_RESPONSE_MESSAGE, null)); // statusCode 204
    }

}

