package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.bookmark.response.BookmarkedPostListResponse;
import com.runninghi.runninghibackv2.domain.entity.Bookmark;
import com.runninghi.runninghibackv2.domain.entity.vo.BookmarkId;
import com.runninghi.runninghibackv2.domain.entity.vo.GpsDataVO;
import com.runninghi.runninghibackv2.domain.repository.BookmarkRepository;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
@Transactional
class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

    private Member member1;
    private Member member2;
    private Post post1;
    private Post post2;
    private Bookmark bookmark1;

    @BeforeEach
    void clear() {
        bookmarkRepository.deleteAllInBatch();
    }

    @BeforeEach
    void setup() {

        member1 = Member.builder()
                .nickname("테스트멤버1")
                .role(Role.USER)
                .build();

        member2 = Member.builder()
                .nickname("테스트멤버2")
                .role(Role.USER)
                .build();

        GpsDataVO gpxDataVO = new GpsDataVO(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f);
        post1 = Post.builder()
                .member(member1)
                .postContent("게시글 내용1")
                .role(Role.USER)
                .locationName("로스엔젤리스")
                .gpsDataVO(gpxDataVO)
                .build();
        post2 = Post.builder()
                .member(member1)
                .postContent("게시글 내용2")
                .role(Role.USER)
                .locationName("도쿄")
                .gpsDataVO(gpxDataVO)
                .build();

        BookmarkId bookmarkId1 = BookmarkId.of(member1.getMemberNo(), post1.getPostNo());
        bookmark1 = Bookmark.builder()
                .bookmarkId(bookmarkId1)
                .member(member1)
                .post(post1)
                .build();


        memberRepository.saveAllAndFlush(Arrays.asList(member1, member2));
        postRepository.saveAllAndFlush(Arrays.asList(post1, post2));
        bookmarkRepository.saveAndFlush(bookmark1);
    }

    @Test
    @DisplayName("특정 회원 북마크 리스트 조회 테스트 : success")
    void testGetBookmarkedPostList () {

        // given
        BookmarkId bookmarkId = BookmarkId.of(member1.getMemberNo(), post2.getPostNo());
        Bookmark bookmark = Bookmark.builder()
                .bookmarkId(bookmarkId)
                .member(member1)
                .post(post2)
                .build();
        bookmarkRepository.saveAndFlush(bookmark);

        // when
        List<BookmarkedPostListResponse> response = bookmarkService.getBookmarkedPostList(member1.getMemberNo());

        // then
        Assertions.assertThat(response)
                .isNotEmpty()
                .hasSize(2)
                .extracting("postNo", Long.class)
                .contains(post1.getPostNo(), post2.getPostNo());

    }

    @ParameterizedTest
    @DisplayName("특정 회원 북마크 리스트 조회 테스트 : 없는 회원이 요청 시 예외 발생 확인")
    @NullSource
    @ValueSource(longs = 0L)
    void testGetBookmarkedPostListException (Long memberNo) {

        // when & then
        Assertions.assertThatThrownBy(
                () -> bookmarkService.getBookmarkedPostList(memberNo)
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("북마크 삭제 테스트 : success")
    void testDeleteBookmark () {

        // given
        Long memberNo = member1.getMemberNo();
        Long postNo = post1.getPostNo();
        Long beforeSize = bookmarkRepository.count();

        // when
        bookmarkService.deleteBookmark(memberNo, postNo);
        Long afterSize = bookmarkRepository.count();

        // then
        Assertions.assertThat(afterSize)
                .isEqualTo(beforeSize - 1L);
    }

    @ParameterizedTest
    @DisplayName("북마크 삭제 테스트 : 존재하지 않는 멤버나 게시글 값일 시 예외 발생 확인")
    @CsvSource(value = {"0, 0", "null, null"}, nullValues = "null")
    void testDeleteBookmarkException(Long memberNo, Long postNo) {

        // when & then
        Assertions.assertThatThrownBy(
                () -> bookmarkService.deleteBookmark(memberNo, postNo)
        ).isInstanceOf(EntityNotFoundException.class);
    }
}