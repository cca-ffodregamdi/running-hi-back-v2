package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.image.response.CreateImageResponse;
import com.runninghi.runninghibackv2.application.dto.image.response.DownloadImageResponse;
import com.runninghi.runninghibackv2.application.service.ImageService;
import com.runninghi.runninghibackv2.application.service.ImageServiceImpl;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "이미지 API", description = "이미지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/image")
public class PostImageController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ImageService imageService;

    private final static String UPLOAD_POST_IMAGE_DIRECTORY = "post/";
    private final static String UPLOAD_IMAGE_RESPONSE_MESSAGE = "이미지 업로드 완료";

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "이미지 업로드 요청",
            description = "이미지를 전달받아 업로드합니다. <br /> MultipartFile 로 전달 받아 Storage에 저장하고 url을 반환합니다.",
            responses = @ApiResponse(responseCode = "200", description = UPLOAD_IMAGE_RESPONSE_MESSAGE)
    )
    public ResponseEntity<ApiResult<String>> saveImages(@Parameter(description = "사용자 인증을 위한 BearerToken")
                                                                           @RequestHeader("Authorization") String token,
                                                                           @Parameter(description = "피드에 업로드할 이미지")
                                                                           @RequestPart("image") MultipartFile imageFile) throws IOException {
        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(token);
        String imageUrl = imageService.uploadImage(imageFile, memberInfo.memberNo(), UPLOAD_POST_IMAGE_DIRECTORY);
        imageService.saveImage(imageUrl);
        return ResponseEntity.ok().body(ApiResult.success(UPLOAD_IMAGE_RESPONSE_MESSAGE, imageUrl));
    }

}
