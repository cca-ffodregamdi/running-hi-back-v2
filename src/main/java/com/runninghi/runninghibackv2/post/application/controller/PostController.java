package com.runninghi.runninghibackv2.post.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.post.application.dto.request.CreatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.response.CreatePostResponse;
import com.runninghi.runninghibackv2.post.application.dto.response.GetPostResponse;
import com.runninghi.runninghibackv2.post.application.dto.response.UpdatePostResponse;
import com.runninghi.runninghibackv2.post.application.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<ApiResult> createRecordAndPost(@RequestBody CreatePostRequest request) {
                                                        // @RequestParam("gpx") MultipartFile gpxFile) {

        CreatePostResponse response = postService.createRecordAndPost(request);
//        CreatePostResponse response = postService.createRecordAndPost(request, gpxFile);

        return ResponseEntity.ok(ApiResult.success("게시글이 성공적으로 등록되었습니다.", response));
    }

    @PutMapping("/api/v1/posts/{postNo}")
    public ResponseEntity<ApiResult> updatePost(@PathVariable Long postNo, @RequestBody UpdatePostRequest request) {

        UpdatePostResponse response = postService.updatePost(postNo, request);

        return ResponseEntity.ok(ApiResult.success("게시글이 성공적으로 수정되었습니다.", response));
    }

    @GetMapping("/api/v1/posts/{postNo}")
    public ResponseEntity<ApiResult> getPost(@PathVariable Long postNo) {

        GetPostResponse response = postService.getPost(postNo);

        return ResponseEntity.ok(ApiResult.success("게시글이 성공적으로 조회되었습니다.", response));
    }

    @DeleteMapping("/v1/posts/{postNo}")
    public ResponseEntity<ApiResult> deletePost(@PathVariable Long postNo) {

        postService.deletePost(postNo);

        return ResponseEntity.ok(ApiResult.success("게시글이 성공적으로 삭제되었습니다.", null));
    }


}
