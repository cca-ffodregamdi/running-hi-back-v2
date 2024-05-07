package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.image.response.CreateImageResponse;
import com.runninghi.runninghibackv2.application.dto.image.response.DownloadImageResponse;
import com.runninghi.runninghibackv2.application.service.ImageService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import java.util.List;

@Tag(name = "이미지 API", description = "이미지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/image")
public class ImageController {

    private final JwtTokenProvider jwtTokenProvider;

    private final ImageService imageService;

    private final static String UPLOAD_IMAGE_RESPONSE_MESSAGE = "이미지 업로드 완료";

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "이미지 업로드 요청",
            description = "이미지를 전달받아 업로드합니다. <br /> 이미지 개수 제한은 6개이며, 리스트 형태(MultipartFile)로 전달 받아 Storage에 저장하고 url을 반환합니다.",
            responses = @ApiResponse(responseCode = "200", description = UPLOAD_IMAGE_RESPONSE_MESSAGE)
    )
    public ResponseEntity<ApiResult<List<CreateImageResponse>>> saveImages(@Parameter(description = "사용자 인증을 위한 BearerToken")
                                                                           @RequestHeader("Authorization") String token,
                                                                           @Parameter(description = "피드에 업로드할 이미지 리스트")
                                                                           @RequestPart("images") @NotBlank List<MultipartFile> imageFiles) {
        AccessTokenInfo memberInfo = jwtTokenProvider.getMemberInfoByBearerToken(token);
        List<CreateImageResponse> imageResponseList = imageService.saveImages(imageFiles, memberInfo.memberNo());
        return ResponseEntity.ok().body(ApiResult.success(UPLOAD_IMAGE_RESPONSE_MESSAGE, imageResponseList));
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "이미지 다운로드", description = "이미지를 다운로드 요청합니다. <br /> 보내진 byte 형태의 파일을 반환하여 다운로드 받게 합니다.",
            responses = @ApiResponse(responseCode = "200", description = "이미지 다운로드 성공")
    )
    public ResponseEntity<byte[]> downloadImage(@Parameter(description = "다운로드 받을 파일 명(경로 포함)")@RequestParam(value = "fileName") @NotBlank String fileName) throws IOException {

        DownloadImageResponse response = imageService.downloadImage(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.imageFileName() + "\"")
                .body(response.imageByte());

    }

}
