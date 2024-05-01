package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.image.response.CreateImageResponse;
import com.runninghi.runninghibackv2.application.dto.image.response.DownloadImageResponse;
import com.runninghi.runninghibackv2.application.service.ImageService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "이미지 API", description = "이미지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/image")
public class ImageController {

    private final JwtTokenProvider jwtTokenProvider;

    private final ImageService imageService;

    @Operation(summary = "이미지 업로드 요청", description = "이미지를 전달받아 업로드합니다. <br /> 이미지 개수 제한은 6개이며, 리스트 형태(MultipartFile)로 전달 받아 Storage에 저장하고 url을 반환합니다.")
    @PostMapping()
    public ResponseEntity<ApiResult<List<CreateImageResponse>>> saveImages(@RequestHeader("Authorization") String token,
                                                                           @RequestPart("images") List<MultipartFile> imageFiles) {
        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(token);
        List<CreateImageResponse> imageResponseList = imageService.saveImages(imageFiles, memberInfo.memberNo());
        return ResponseEntity.ok().body(ApiResult.success("업로드 완료", imageResponseList));
    }

    @Operation(summary = "이미지 다운로드", description = "이미지를 다운로드 요청합니다. <br /> 보내진 byte 형태의 파일을 반환하여 다운로드 받게 합니다.")
    @GetMapping("download")
    public ResponseEntity<byte[]> downloadImage(@RequestParam(value = "filename") String fileName) throws IOException {

        DownloadImageResponse response = imageService.downloadImage(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.imageFileName() + "\"")
                .body(response.imageByte());

    }

}
