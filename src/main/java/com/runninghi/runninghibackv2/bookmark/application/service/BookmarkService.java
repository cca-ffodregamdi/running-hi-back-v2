package com.runninghi.runninghibackv2.bookmark.application.service;

import com.runninghi.runninghibackv2.bookmark.application.dto.request.CreateBookmarkRequest;
import com.runninghi.runninghibackv2.bookmark.application.dto.response.BookmarkedPostListResponse;
import com.runninghi.runninghibackv2.bookmark.application.dto.response.CreateBookmarkResponse;
import com.runninghi.runninghibackv2.bookmark.domain.aggregate.entity.Bookmark;
import com.runninghi.runninghibackv2.bookmark.domain.aggregate.vo.BookmarkId;
import com.runninghi.runninghibackv2.bookmark.domain.repository.BookmarkRepository;
import com.runninghi.runninghibackv2.bookmark.domain.service.ApiBookmarkService;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private ApiBookmarkService apiBookmarkService;


    /**
     * 북마크 탭 클릭 시 해당 유저의 북마크 게시글 리스트가 조회되는 메소드
     * @param memberNo
     * @return 리스트 형태의 게시글 정보, 기록
     */
    @Transactional(readOnly = true)
    public  List<BookmarkedPostListResponse> getBookmarkedPostList(Long memberNo) {

        List<Bookmark> bookmarkListResult = bookmarkRepository.findAllByBookmarkId_MemberNo(memberNo);
        if (bookmarkListResult.isEmpty()) throw new EntityNotFoundException("일치하는 항목이 없습니다.");

        return bookmarkListResult.stream().map(i -> BookmarkedPostListResponse.fromEntity(i.getPost())).toList();

    }

    /**
     * 게시글 북마크 추가 메소드
     * @param request(memberNo, postNo)
     * @return CreateBookmarkResponse(memberNo, postNo)
     */
    @Transactional
    public CreateBookmarkResponse createBookmark(CreateBookmarkRequest request) {

        BookmarkId bookmarkId = BookmarkId.builder()
                                        .memberNo(request.memberNo())
                                        .postNo(request.postNo())
                                        .build();

        Member member = apiBookmarkService.getMemberById(request.memberNo());
        Post post = apiBookmarkService.getPostById(request.postNo());


        Bookmark bookmark = Bookmark.builder()
                                .bookmarkId(bookmarkId)
                                .member(member)
                                .post(post)
                                .build();

        return CreateBookmarkResponse.fromEntity(bookmarkRepository.save(bookmark));
    }

    /**
     * 북마크 취소 메소드
     * @param memberNo
     * @param postNo
     */
    @Transactional
    public void deleteBookmark(Long memberNo, Long postNo) {

        bookmarkRepository.deleteById(
                BookmarkId.builder()
                        .memberNo(memberNo)
                        .postNo(postNo)
                        .build()
        );

    }
}
