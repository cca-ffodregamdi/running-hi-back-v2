package com.runninghi.runninghibackv2.keyword.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import com.runninghi.runninghibackv2.keyword.application.dto.request.KeywordRequest;
import com.runninghi.runninghibackv2.keyword.application.dto.response.KeywordResponse;
import com.runninghi.runninghibackv2.keyword.application.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;


    @PostMapping("/api/v1/keywords")
    public ResponseEntity<ApiResult> createKeyword(@RequestBody KeywordRequest request) {

        try {

            KeywordResponse response = keywordService.createKeyword(request);
            return ResponseEntity.ok(ApiResult.success("성공적으로 등록되었습니다.", response));

        } catch (Exception e){
            return ResponseEntity.ok(ApiResult.error(ErrorCode.BAD_REQUEST));
        }


    }

    @GetMapping("/api/v1/keywords")
    public ResponseEntity<ApiResult> getKeyword(@RequestBody KeywordRequest request) {

        try {

            KeywordResponse response = keywordService.getKeyword(request);
            return ResponseEntity.ok(ApiResult.success("성공적으로 조회되었습니다.", response));

        } catch (Exception e) {
            return ResponseEntity.ok(ApiResult.error(ErrorCode.NOT_FOUND));
        }
    }


}
