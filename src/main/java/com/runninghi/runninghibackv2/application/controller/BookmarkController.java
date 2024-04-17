package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.application.dto.bookmark.request.CreateBookmarkRequest;
import com.runninghi.runninghibackv2.application.dto.bookmark.response.BookmarkedPostListResponse;
import com.runninghi.runninghibackv2.application.dto.bookmark.response.CreateBookmarkResponse;
import com.runninghi.runninghibackv2.application.service.BookmarkService;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 북마크 탭 클릭 시 요청한 사용자의 북마크된 Post들을 응답해주는 메소드입니다.
     * -> 만약 다른 회원의 북마크 리스트를 보게 한다면 리팩토링 필요
     * @param bearerToken HttpServletRequest Header에 담긴 Authorization
     * @return Bookmark 테이블에서 조회된 Posts
     */
    @GetMapping()
    public ResponseEntity<ApiResult> getBookmarkedPostList(@RequestHeader(name = "Authorization") String bearerToken) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        List<BookmarkedPostListResponse> bookmarkedPostList = bookmarkService.getBookmarkedPostList(memberInfo.memberNo());

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 조회되었습니다.", bookmarkedPostList));
    }

    @PostMapping()
    public ResponseEntity<ApiResult> createBookmark (@RequestHeader("Authorization") String bearerToken,
                                                    @RequestBody(required = true) Long postNo) {

        AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        CreateBookmarkRequest request = CreateBookmarkRequest.of(accessTokenInfo.memberNo(), postNo);
        CreateBookmarkResponse response = bookmarkService.createBookmark(request);

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 저장되었습니다.", response));
    }

    @DeleteMapping(value = "/{postNo}")
    public ResponseEntity<ApiResult> deleteBookmark (@RequestHeader("Authorization") String bearerToken,
                                                    @PathVariable(name = "postNo") Long postNo) {

            AccessTokenInfo accessTokenInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
            bookmarkService.deleteBookmark(accessTokenInfo.memberNo(), postNo);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResult.success("성공적으로 삭제되었습니다.", null)); // statusCode 204
    }

}
