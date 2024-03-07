package com.runninghi.runninghibackv2.keyword.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.keyword.application.dto.request.CreateKeywordRequest;
import com.runninghi.runninghibackv2.keyword.application.dto.response.CreateKeywordResponse;
import com.runninghi.runninghibackv2.keyword.application.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;


    @PostMapping("/api/v1/keywords")
    public ResponseEntity<ApiResult> createBookmarkFolder(@RequestBody CreateKeywordRequest request) {

        CreateKeywordResponse response = keywordService.createKeyword(request);

        return ResponseEntity.ok(ApiResult.success("성공적으로 등록되었습니다.", response));
    }


}
