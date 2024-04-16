package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.bookmark.request.CreateBookmarkRequest;
import com.runninghi.runninghibackv2.application.dto.bookmark.response.BookmarkedPostListResponse;
import com.runninghi.runninghibackv2.application.dto.bookmark.response.CreateBookmarkResponse;
import com.runninghi.runninghibackv2.domain.entity.Bookmark;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.vo.BookmarkId;
import com.runninghi.runninghibackv2.domain.repository.BookmarkRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public  List<BookmarkedPostListResponse> getBookmarkedPostList(Long memberNo) {
        List<Bookmark> bookmarkListResult = bookmarkRepository.findAllByBookmarkId_MemberNo(memberNo);
        if (bookmarkListResult.isEmpty()) throw new EntityNotFoundException();

        return bookmarkListResult.stream().map(i -> BookmarkedPostListResponse.fromEntity(i.getPost())).toList();
    }

    @Transactional
    public CreateBookmarkResponse createBookmark(CreateBookmarkRequest request) {

        BookmarkId bookmarkId = BookmarkId.of(request.memberNo(), request.postNo());

        Member member = memberRepository.findByMemberNo(request.memberNo());
        Post post = postRepository.findById(request.postNo())
                .orElseThrow(EntityNotFoundException::new);

        Bookmark bookmark = Bookmark.builder()
                                .bookmarkId(bookmarkId)
                                .member(member)
                                .post(post)
                                .build();

        return CreateBookmarkResponse.fromEntity(bookmarkRepository.save(bookmark));
    }

    @Transactional
    public void deleteBookmark(Long memberNo, Long postNo) {

        Bookmark bookmark = bookmarkRepository.findById(BookmarkId.of(memberNo,postNo))
                        .orElseThrow(EntityNotFoundException::new);
        bookmarkRepository.delete(bookmark);
    }

}
