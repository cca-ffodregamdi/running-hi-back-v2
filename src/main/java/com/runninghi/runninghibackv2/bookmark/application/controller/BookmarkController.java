package com.runninghi.runninghibackv2.bookmark.application.controller;

import com.runninghi.runninghibackv2.bookmark.application.dto.request.CreateBookmarkRequest;
import com.runninghi.runninghibackv2.bookmark.application.dto.response.BookmarkedPostListResponse;
import com.runninghi.runninghibackv2.bookmark.application.dto.response.CreateBookmarkResponse;
import com.runninghi.runninghibackv2.bookmark.application.service.BookmarkService;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "api/v1/bookmark")
public class BookmarkController {

    private BookmarkService bookmarkService;

    /**
     * 북마크 탭 클릭 시 요청한 사용자의 북마크된 Post들을 응답해주는 메소드입니다.
     * @param memberNo 작성자 번호
     * @return Bookmark 테이블에서 조회된 Posts
     */
    @GetMapping(name = "/{memberNo}")
    public ResponseEntity<ApiResult> getBookmarkedPostList(@PathVariable(name = "memberNo") Long memberNo) {

        List<BookmarkedPostListResponse> bookmarkedPostList = bookmarkService.getBookmarkedPostList(memberNo);

        return ResponseEntity.ok().body(ApiResult.success("성공적으로 조회되었습니다.", bookmarkedPostList));
    }

    @PostMapping()
    public ResponseEntity<ApiResult> createBookmark (@RequestBody CreateBookmarkRequest request) {

        CreateBookmarkResponse response = bookmarkService.createBookmark(request);
        return ResponseEntity.ok().body(ApiResult.success("성공적으로 저장되었습니다.", response));
    }

    @DeleteMapping()
    public ResponseEntity<ApiResult> deleteBookmark (@RequestParam(name = "memberNo") Long memberNo,
                                                     @RequestParam(name = "postNo") Long postNo) {
            bookmarkService.deleteBookmark(memberNo, postNo);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResult.success("성공적으로 삭제되었습니다.", null)); // statusCode 204
    }

}
