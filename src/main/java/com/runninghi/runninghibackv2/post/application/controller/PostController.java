package com.runninghi.runninghibackv2.post.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.post.application.dto.request.CreatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.response.CreatePostResponse;
import com.runninghi.runninghibackv2.post.application.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/v1/posts")
    public ResponseEntity<ApiResult> createRecordAndPost(@RequestBody CreatePostRequest request,
                                                         @RequestParam("gpx") MultipartFile gpxFile) {

        CreatePostResponse response = postService.createRecordAndPost(request, gpxFile);

        return ResponseEntity.ok(ApiResult.success("게시글이 성공적으로 등록되었습니다.", response));
    }

}
