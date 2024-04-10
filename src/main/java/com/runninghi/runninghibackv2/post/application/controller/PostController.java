package com.runninghi.runninghibackv2.post.application.controller;

import com.runninghi.runninghibackv2.common.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.post.application.dto.request.CreatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.response.CreatePostResponse;
import com.runninghi.runninghibackv2.post.application.dto.response.GetAllPostsResponse;
import com.runninghi.runninghibackv2.post.application.dto.response.GetPostResponse;
import com.runninghi.runninghibackv2.post.application.dto.response.UpdatePostResponse;
import com.runninghi.runninghibackv2.post.application.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Tag(name = "게시글 컨트롤러", description = "게시글 작성, 조회, 수정, 삭제 API")
@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private  final JwtTokenProvider jwtTokenProvider;

    private static final String GET_MAPPING_RESPONSE_MESSAGE = "성공적으로 조회되었습니다.";
    private static final String CREATE_RESPONSE_MESSAGE = "성공적으로 생성되었습니다.";
    private static final String UPDATE_RESPONSE_MESSAGE = "성공적으로 수정되었습니다.";
    private static final String DELETE_RESPONSE_MESSAGE = "성공적으로 삭제되었습니다.";

    @GetMapping
    public ResponseEntity<ApiResult> getAllPosts(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<GetAllPostsResponse> response = postService.getPostScroll(pageable);

        return ResponseEntity.ok(ApiResult.success( GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @PostMapping
    public ResponseEntity<ApiResult> createRecordAndPost(@RequestHeader("Authorization") String bearerToken,
                                                         @RequestPart("title") String postTitle,
                                                         @RequestPart("content") String postContent,
                                                         @RequestPart("location") String locationName,
                                                         @RequestPart("keyword") List<String> keywordList,
                                                         @RequestPart("gpx") MultipartFile gpxFile) throws ParserConfigurationException, IOException, SAXException {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        CreatePostRequest request = new CreatePostRequest(memberInfo.memberNo(), memberInfo.role(), postTitle, postContent, locationName, keywordList);

        CreatePostResponse response = postService.createRecordAndPost(request, gpxFile.getResource());

        return ResponseEntity.ok(ApiResult.success(CREATE_RESPONSE_MESSAGE, response));
    }

    @PutMapping("/{postNo}")
    public ResponseEntity<ApiResult> updatePost(@RequestHeader(name = "Authorization") String bearerToken,
                                                @PathVariable Long postNo, @RequestBody UpdatePostRequest request) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        UpdatePostResponse response = postService.updatePost(memberInfo.memberNo(), postNo, request);

        return ResponseEntity.ok(ApiResult.success(UPDATE_RESPONSE_MESSAGE, response));
    }

    @GetMapping("/{postNo}")
    public ResponseEntity<ApiResult> getPost(@PathVariable Long postNo) {

        GetPostResponse response = postService.getPost(postNo);

        return ResponseEntity.ok(ApiResult.success( GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @DeleteMapping("/{postNo}")
    public ResponseEntity<ApiResult> deletePost(@RequestHeader(name = "Authorization") String bearerToken,
                                                @PathVariable Long postNo) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        postService.deletePost(memberInfo.memberNo(), postNo);

        return ResponseEntity.ok(ApiResult.success(DELETE_RESPONSE_MESSAGE, null));
    }

    @HasAccess
    @GetMapping(value = "/reported")
    public ResponseEntity<ApiResult> getReportedPostList(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                         @RequestParam(defaultValue = "10") @Positive int size,
                                                         @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));

        Page<GetAllPostsResponse> response = postService.getReportedPostScroll(pageable);

        return ResponseEntity.ok(ApiResult.success( GET_MAPPING_RESPONSE_MESSAGE, response));
    }


}
