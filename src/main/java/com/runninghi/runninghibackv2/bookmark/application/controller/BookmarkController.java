package com.runninghi.runninghibackv2.bookmark.application.controller;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.bookmark.application.dto.request.CreateBookmarkRequest;
import com.runninghi.runninghibackv2.bookmark.application.dto.response.BookmarkedPostListResponse;
import com.runninghi.runninghibackv2.bookmark.application.dto.response.CreateBookmarkResponse;
import com.runninghi.runninghibackv2.bookmark.application.service.BookmarkService;
import com.runninghi.runninghibackv2.common.dto.MemberJwtInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "api/v1/bookmark")
public class BookmarkController {

    private BookmarkService bookmarkService;
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 북마크 탭 클릭 시 요청한 사용자의 북마크된 Post들을 응답해주는 메소드입니다.
     * @param servletRequest HttpServletRequest
     * @return Bookmark 테이블에서 조회된 Posts
     */
    @GetMapping()
    public ResponseEntity<ApiResult> getBookmarkedPostList(HttpServletRequest servletRequest) {

        MemberJwtInfo memberInfo = jwtTokenProvider.getMemberNoAndRoleFromRequest(servletRequest);
        List<BookmarkedPostListResponse> bookmarkedPostList = bookmarkService.getBookmarkedPostList(memberInfo.memberNo());

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 조회되었습니다.", bookmarkedPostList));
    }

    @PostMapping()
    public ResponseEntity<ApiResult> createBookmark (HttpServletRequest servletRequest,
                                                    @RequestBody(required = true) Long postNo) {

        MemberJwtInfo memberJwtInfo = jwtTokenProvider.getMemberNoAndRoleFromRequest(servletRequest);
        CreateBookmarkRequest request = CreateBookmarkRequest.of(memberJwtInfo.memberNo(), postNo);
        CreateBookmarkResponse response = bookmarkService.createBookmark(request);

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 저장되었습니다.", response));
    }

    @DeleteMapping("/{postNo}")
    public ResponseEntity<ApiResult> deleteBookmark (HttpServletRequest servletRequest,
                                                     @PathVariable(name = "postNo") Long postNo) {

            MemberJwtInfo memberJwtInfo = jwtTokenProvider.getMemberNoAndRoleFromRequest(servletRequest);
            bookmarkService.deleteBookmark(memberJwtInfo.memberNo(), postNo);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResult.success("성공적으로 삭제되었습니다.", null)); // statusCode 204
    }

}
