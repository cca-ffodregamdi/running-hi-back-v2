package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.post.request.CreatePostRequest;
import com.runninghi.runninghibackv2.application.dto.post.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.application.dto.post.response.*;
import com.runninghi.runninghibackv2.application.service.PostService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.common.response.PageResult;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "게시글/러닝코스 API", description = "게시글 작성, 조회, 수정, 삭제와 해당 게시글의 러닝코스 데이터를 조회하는 API 입니다.")
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


    /*
    * 게시글 조회
    * 1. 전체 게시글 조회 (최신순/추천순 + 지역별 필터 가능)
    * 2. 게시글 상세보기
    * 3. 나의 게시글 조회 (최신순)
    * 4. 나의 좋아요 게시글 조회 (최신순)
    * 5. 나의 북마크 게시글 조회 (최신순)
    * 6. 신고된 게시글 조회
    */

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 리스트 조회", description = "게시글 전체 리스트를 조회합니다.\n 난이도/지열별로 필터링이 가능합니다.")
    public ResponseEntity<PageResult<GetAllPostsResponse>> getAllPosts(@RequestHeader("Authorization") String bearerToken,
                                                                       @RequestParam(defaultValue = "1") @PositiveOrZero int page,
                                                                       @RequestParam(defaultValue = "10") @Positive int size,
                                                                       @RequestParam(defaultValue = "latest") String sort,
                                                                       @RequestParam(defaultValue = "1") @Positive int distance) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        Pageable pageable = PageRequest.of(page - 1 , size);

        PageResultData<GetAllPostsResponse> response = postService.getPostScroll(memberInfo.memberNo(), pageable, sort, distance);

        return ResponseEntity.ok(PageResult.success(GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @GetMapping("/{postNo}")
    @Operation(summary = "게시글 상세보기", description = "게시글 클릭시 상세보기 가능합니다.")
    public ResponseEntity<ApiResult<GetPostResponse>> getPost(@RequestHeader("Authorization") String bearerToken,
                                                              @PathVariable Long postNo) {
        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);
        GetPostResponse response = postService.getPostDetailByPostNo(memberInfo.memberNo(), postNo);

        return ResponseEntity.ok(ApiResult.success( GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @GetMapping(value = "my-feed",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "나의 게시글 리스트 조회", description = "나의 게시글 전체 리스트를 조회합니다. 공유 여부가 함께 조회됩니다.")
    public ResponseEntity<PageResult<GetMyPostsResponse>> getMyPosts(@RequestHeader("Authorization") String bearerToken,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        Pageable pageable = PageRequest.of(page - 1 , size);

        PageResultData<GetMyPostsResponse> response = postService.getMyPostsScroll(pageable, memberInfo.memberNo());

        return ResponseEntity.ok(PageResult.success(GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @GetMapping(value = "liked",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "나의 좋아요 게시글 리스트 조회", description = "나의 좋아요된 게시글 전체 리스트를 조회합니다.")
    public ResponseEntity<PageResult<GetAllPostsResponse>> getMyLikedPosts(@RequestHeader("Authorization") String bearerToken,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        Pageable pageable = PageRequest.of(page - 1 , size);

        PageResultData<GetAllPostsResponse> response = postService.getMyLikedPosts(pageable, memberInfo.memberNo());

        return ResponseEntity.ok(PageResult.success(GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @GetMapping(value = "bookmarked",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "나의 북마크 게시글 리스트 조회", description = "나의 북마크된 게시글 전체 리스트를 조회합니다.")
    public ResponseEntity<PageResult<GetAllPostsResponse>> getMyBookmarkedPosts(@RequestHeader("Authorization") String bearerToken,
                                                                          @RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        Pageable pageable = PageRequest.of(page - 1 , size);

        PageResultData<GetAllPostsResponse> response = postService.getMyBookmarkedPosts(pageable, memberInfo.memberNo());

        return ResponseEntity.ok(PageResult.success(GET_MAPPING_RESPONSE_MESSAGE, response));
    }

    @HasAccess
    @GetMapping(value = "/reported", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "신고된 게시글 조회", description = "신고된 게시글만 볼 수 있습니다.")
    public ResponseEntity<ApiResult<Page<GetReportedPostsResponse>>> getReportedPostList(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                                         @RequestParam(defaultValue = "10") @Positive int size,
                                                                                         @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));

        Page<GetReportedPostsResponse> response = postService.getReportedPostScroll(pageable);

        return ResponseEntity.ok(ApiResult.success( GET_MAPPING_RESPONSE_MESSAGE, response));
    }


    /*
    * 게시글 공유
    * 1. 러닝 직후 gpx 데이터 저장 (status = false)
    * 2. 게시글 공유 (status = true)
    * 3. 공유된 게시글 수정
    */


    @PostMapping(value = "/gps", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "러닝 데이터 저장", description = "러닝이 끝난 직후 txt 형식의 gps 파일을 저장합니다. ")
    public ResponseEntity<ApiResult<CreateRecordResponse>> createGPSRecord(@RequestHeader("Authorization") String bearerToken,
                                                                               @RequestPart("file") MultipartFile file) throws IOException {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        CreateRecordResponse response = postService.createRecord(memberInfo.memberNo(), file);

        return ResponseEntity.ok(ApiResult.success(CREATE_RESPONSE_MESSAGE, response));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "저장된 러닝 데이터 게시글 공유", description = "러닝 데이터를 게시글로 공유합니다. \n 게시글 제목, 내용, 사진url을 추가로 받습니다.")
    public ResponseEntity<ApiResult<CreatePostResponse>> postGPSRecord(@RequestHeader("Authorization") String bearerToken,
                                                                        @RequestBody CreatePostRequest request) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        CreatePostResponse response = postService.createPost(memberInfo.memberNo(), request);

        return ResponseEntity.ok(ApiResult.success(CREATE_RESPONSE_MESSAGE, response));
    }

    @PutMapping(value = "/{postNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 수정", description = "공유된 게시글의 제목/내용을 수정합니다. \n 러닝기록은 수정이 불가능합니다.")
    public ResponseEntity<ApiResult<UpdatePostResponse>> updatePost(@RequestHeader(name = "Authorization") String bearerToken,
                                                                    @PathVariable Long postNo, @RequestBody UpdatePostRequest request) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        UpdatePostResponse response = postService.updatePost(memberInfo.memberNo(), postNo, request);

        return ResponseEntity.ok(ApiResult.success(UPDATE_RESPONSE_MESSAGE, response));
    }


    /*게시글 삭제*/
    @DeleteMapping(value = "/{postNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    public ResponseEntity<ApiResult<DeletePostResponse>> deletePost(@RequestHeader(name = "Authorization") String bearerToken,
                                                      @PathVariable Long postNo) {

        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(bearerToken);

        DeletePostResponse response = postService.deletePost(memberInfo.memberNo(), postNo);

        return ResponseEntity.ok(ApiResult.success(DELETE_RESPONSE_MESSAGE, response));
    }

//    @GetMapping(value = "/coordinate/{postNo}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "GPX 경도, 위도 조회", description = " '나도 이 코스 달리기' 선택시 반환되는 데이터입니다. \n 지도 상 표기를 위해 경도-위도 값이 튜플 형태로 반환됩니다.")
//    public ResponseEntity<ApiResult<GpsDataResponse>> getGPXData(@PathVariable Long postNo) throws IOException {
//
//        GpsDataResponse response = postService.getGpxLonLatData(postNo);
//
//        return ResponseEntity.ok(ApiResult.success(GET_MAPPING_RESPONSE_MESSAGE, response));
//    }

}
