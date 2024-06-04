package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.faq.request.CreateFaqRequest;
import com.runninghi.runninghibackv2.application.dto.faq.request.UpdateFaqRequest;
import com.runninghi.runninghibackv2.application.dto.faq.response.CreateFaqResponse;
import com.runninghi.runninghibackv2.application.dto.faq.response.DeleteFaqResponse;
import com.runninghi.runninghibackv2.application.dto.faq.response.GetFaqResponse;
import com.runninghi.runninghibackv2.application.dto.faq.response.UpdateFaqResponse;
import com.runninghi.runninghibackv2.application.service.FaqService;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    /**
     * FAQ 생성 메소드입니다.
     * @param request FAQ 생성 요청 데이터
     * @return 생성된 FAQ 정보
     */
    @HasAccess
    @PostMapping(value = "/api/v1/faq", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "FAQ 생성",
            description = "새로운 FAQ를 생성합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
            },
            responses = @ApiResponse(responseCode = "200", description = "FAQ 생성 성공")
    )
    public ResponseEntity<ApiResult<CreateFaqResponse>> createFaq(
            @RequestBody CreateFaqRequest request
    ) {

        CreateFaqResponse response = faqService.createFaq(request);

        return ResponseEntity.ok(ApiResult.success("FAQ 생성 성공", response));
    }

    /**
     * FAQ 수정 메소드입니다.
     * @param faqNo FAQ ID
     * @param request FAQ 수정 요청 데이터
     * @return 수정된 FAQ 정보
     */
    @HasAccess
    @PutMapping(value = "/api/v1/faq/{faqNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "FAQ 수정",
            description = "기Faq 수정합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "faqNo", description = "수정하고자 하는 FAQ의 고유 번호", required = true)
            },
            responses = @ApiResponse(responseCode = "200", description = "FAQ 수정 성공")
    )
    public ResponseEntity<ApiResult<UpdateFaqResponse>> updateFaq(
            @Parameter(description = "FAQ ID") @PathVariable Long faqNo,
            @RequestBody UpdateFaqRequest request
    ) {

        UpdateFaqResponse response = faqService.updateFaq(faqNo, request);

        return ResponseEntity.ok(ApiResult.success("FAQ 수정 성공", response));
    }

    /**
     * FAQ 조회 메소드입니다.
     * @param faqNo FAQ ID
     * @return 조회된 FAQ 정보
     */
    @GetMapping(value = "/api/v1/faq/{faqNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "FAQ 조회",
            description = "특정 FAQ를 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "faqNo", description = "조회하고자 하는 FAQ의 고유 번호", required = true)
            },
            responses = @ApiResponse(responseCode = "200", description = "FAQ 조회 성공")
    )
    public ResponseEntity<ApiResult<GetFaqResponse>> getFaq(
            @Parameter(description = "FAQ ID") @PathVariable Long faqNo
    ) {

        GetFaqResponse response = faqService.getFaq(faqNo);

        return ResponseEntity.ok(ApiResult.success("FAQ 조회 성공", response));
    }

    /**
     * 모든 FAQ 리스트 조회 메소드입니다.
     * @return 조회된 모든 FAQ 리스트
     */
    @GetMapping(value = "/api/v1/faq", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "모든 FAQ 조회",
            description = "모든 FAQ 리스트를 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
            },
            responses = @ApiResponse(responseCode = "200", description = "모든 FAQ 조회 성공")
    )
    public ResponseEntity<ApiResult<List<GetFaqResponse>>> getAllFaqs() {
        List<GetFaqResponse> response = faqService.getAllFaq();

        return ResponseEntity.ok(ApiResult.success("모든 FAQ 조회 성공", response));
    }

    /**
     * FAQ 삭제 메소드입니다.
     * @param faqNo FAQ ID
     * @return 삭제 성공 여부
     */
    @HasAccess
    @DeleteMapping(value = "/api/v1/faq/{faqNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "FAQ 삭제",
            description = "기존 FAQ를 삭제합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "faqNo", description = "삭제하고자 하는 FAQ의 고유 번호", required = true)
            },
            responses = @ApiResponse(responseCode = "200", description = "FAQ 삭제 성공")
    )
    public ResponseEntity<ApiResult<DeleteFaqResponse>> deleteFaq(
            @Parameter(description = "FAQ ID") @PathVariable Long faqNo
    ) {
        DeleteFaqResponse response = faqService.deleteFaq(faqNo);
        return ResponseEntity.ok(ApiResult.success("FAQ 삭제 성공", response));
    }



}
