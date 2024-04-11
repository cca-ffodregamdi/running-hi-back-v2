package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.application.dto.keyword.request.KeywordRequest;
import com.runninghi.runninghibackv2.application.dto.keyword.response.KeywordResponse;
import com.runninghi.runninghibackv2.application.service.KeywordService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Hidden
public class KeywordController {

    private final KeywordService keywordService;


    @PostMapping("/api/v1/keywords")
    public ResponseEntity<ApiResult> createKeyword(@RequestBody KeywordRequest request) {

        KeywordResponse response = keywordService.createKeyword(request);
        return ResponseEntity.ok(ApiResult.success("성공적으로 등록되었습니다.", response));

    }

}
