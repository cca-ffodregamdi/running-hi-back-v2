package com.runninghi.runninghibackv2.post.application.service;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.keyword.domain.repository.KeywordRepository;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.post.application.dto.request.CreatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.response.CreatePostResponse;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.post.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.PostKeyword;
import com.runninghi.runninghibackv2.post.domain.repository.PostKeywordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PostServiceTests {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostKeywordService postKeywordService;

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    InputStream inputStream = getClass().getResourceAsStream("/data.gpx");
    InputStreamResource inputStreamResource;

    {
        assert inputStream != null;
        inputStreamResource = new InputStreamResource(inputStream);
    }

    @BeforeEach
    @AfterEach
    void clear() {
        postKeywordRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        keywordRepository.deleteAllInBatch();
    }

    Member member;
    Member admin;
    Keyword keyword;

    @BeforeEach
    void setup() {
        member = new Member.MemberBuilder()
                .nickname("member")
                .role(Role.USER)
                .build();


        admin = new Member.MemberBuilder()
                .nickname("admin")
                .role(Role.ADMIN)
                .build();

        keyword = new Keyword("기존 키워드");

        memberRepository.saveAndFlush(member);
        memberRepository.saveAndFlush(admin);
        keywordRepository.saveAndFlush(keyword);
    }

    @Test
    @DisplayName("게시글 생성 테스트 : success")
    void testCreatePostSuccess() throws ParserConfigurationException, IOException, SAXException {
        // Given
        long beforeSize = postRepository.count();
        long keywordBeforeSize = keywordRepository.count();

        // When
        String postTitle = "Test Post";
        String postContent = "Test Post Content 입니다.";
        String locationName = "Location";
        List<String> keywordList = List.of("새 키워드");

        CreatePostRequest request = new CreatePostRequest(
                member.getMemberNo(),
                member.getRole(),
                postTitle,
                postContent,
                locationName,
                keywordList
        );

        CreatePostResponse response = postService.createRecordAndPost(request, inputStreamResource);
        long afterSize = postRepository.count();
        long keywordAfterSize= keywordRepository.count();

        Optional<Post> post = postRepository.findById(response.postNo());

        // Then
        assertEquals(beforeSize + 1, afterSize);
//        assertEquals(member.getMemberNo(), post.get().getMember().getMemberNo());
        assertEquals(postTitle, post.get().getPostTitle());
        assertEquals(postContent, post.get().getPostContent());
        assertEquals(locationName, post.get().getLocationName());
        assertEquals(0, post.get().getReportCnt());

        //키워드 생성 확인
        assertEquals(keywordBeforeSize + 1, keywordAfterSize);

        //Post_Keyword 저장 확인
        List<PostKeyword> keywords = postKeywordService.getKeywordsByPost(post.get());
        for (PostKeyword postKeyword : keywords) {
            String keywordName = postKeyword.getKeyword().getKeywordName();
            assertTrue(keywordList.contains(keywordName));
        }

    }

    @Test
    @DisplayName("게시글 생성 테스트 : 존재하는 키워드는 저장하지 않음")
    void testCreateKeywordThatAlreadyExists() throws ParserConfigurationException, IOException, SAXException {
        // Given
        long keywordBeforeSize = keywordRepository.count();

        // When
        String postTitle = "Test Post";
        String postContent = "Test Post Content 입니다.";
        String locationName = "Location";
        List<String> keywordList = List.of("기존 키워드", "새 키워드");

        CreatePostRequest request = new CreatePostRequest(
                member.getMemberNo(),
                member.getRole(),
                postTitle,
                postContent,
                locationName,
                keywordList
        );

        postService.createRecordAndPost(request, inputStreamResource);
        long keywordAfterSize= keywordRepository.count();

        // Then
        assertEquals(keywordBeforeSize + 1, keywordAfterSize);
    }

    @Test
    @DisplayName("게시글 생성 테스트 : 제목 공백 예외처리")
    void testTitleIsNullException() {
        // Given
        String postTitle = "  ";
        String postContent = "Test Post Content 입니다.";
        String locationName = "Location";
        List<String> keywordList = List.of("테스트");

        CreatePostRequest request = new CreatePostRequest(
                member.getMemberNo(),
                member.getRole(),
                postTitle,
                postContent,
                locationName,
                keywordList
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postService.createRecordAndPost(request, inputStreamResource));

        assertEquals("제목은 1글자 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("게시글 생성 테스트 : 제목 공백 예외처리")
    void testContentIsNullException() {
        // Given
        String postTitle = "Test Post";
        String postContent = "   ";
        String locationName = "Location";
        List<String> keywordList = List.of("테스트");

        CreatePostRequest request = new CreatePostRequest(
                member.getMemberNo(),
                member.getRole(),
                postTitle,
                postContent,
                locationName,
                keywordList
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postService.createRecordAndPost(request, inputStreamResource));

        assertEquals("내용은 1글자 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("게시글 삭제 테스트 : success")
    void testDeletePostSuccess() throws ParserConfigurationException, IOException, SAXException {
        // Given
        String postTitle = "Test Post";
        String postContent = "Test Post Content 입니다.";
        String locationName = "Location";
        List<String> keywordList = List.of("기존 키워드", "새 키워드");

        CreatePostRequest request = new CreatePostRequest(
                member.getMemberNo(),
                member.getRole(),
                postTitle,
                postContent,
                locationName,
                keywordList
        );

        CreatePostResponse response = postService.createRecordAndPost(request, inputStreamResource);

        long beforeSize = postRepository.count();
        long postKeywordBefore = postKeywordRepository.count();


        // When
        postService.deletePost(response.postNo());

        long afterSize = postRepository.count();
        long postKeywordAfter= postKeywordRepository.count();

        // Then
        assertEquals(afterSize + 1, beforeSize);

        //Post_Keyword 삭제 확인
        assertEquals(postKeywordAfter + 2, postKeywordBefore);
    }

    @Test
    @DisplayName("게시글 수정 테스트 : success")
    void testUpdatePostSuccess() throws ParserConfigurationException, IOException, SAXException {
        // Given
        String postTitle = "Test Post";
        String postContent = "Test Post Content 입니다.";
        String locationName = "Location";
        List<String> keywordList = List.of("기존 키워드", "새 키워드");

        CreatePostRequest request = new CreatePostRequest(
                member.getMemberNo(),
                member.getRole(),
                postTitle,
                postContent,
                locationName,
                keywordList
        );

        CreatePostResponse response = postService.createRecordAndPost(request, inputStreamResource);

        // When
        String updateTitle = "Test Post";
        String updateContent = "Test Post Content 입니다.";
        List<String> updateList = List.of("기존 키워드", "새 키워드2");

        UpdatePostRequest updateRequest = new UpdatePostRequest(
                updateTitle,
                updateContent,
                updateList
        );

        postService.updatePost(response.postNo(), updateRequest);

        Optional<Post> post = postRepository.findById(response.postNo());

        // Then
        assertEquals(post.get().getPostTitle(), updateTitle);
        assertEquals(post.get().getPostContent(), updateContent);

        //Post_Keyword 확인
        List<PostKeyword> keywords = postKeywordService.getKeywordsByPost(post.get());
        for (PostKeyword postKeyword : keywords) {
            String keywordName = postKeyword.getKeyword().getKeywordName();
            assertTrue(updateList.contains(keywordName));
        }

    }
}
