package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.feedback.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.response.*;
import com.runninghi.runninghibackv2.application.service.FeedbackService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.annotations.HasAccess;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "피드백 API", description = "피드백/문의사항 관련 API")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자가 피드백/문의사항을 작성하는 API입니다.
     *
     * <p>이 메소드는 사용자가 시스템에 피드백이나 문의사항을 제출할 수 있게 합니다. 사용자 인증은
     * Bearer 토큰을 통해 이루어지며, 피드백 내용은 JSON 형태로 전달받습니다.</p>
     *
     * @param token 사용자 인증을 위한 Bearer 토큰입니다. 헤더에 포함되어야 합니다.
     * @param request 사용자가 제출한 피드백 내용을 담고 있는 객체입니다.
     * @return 피드백이 성공적으로 저장되었음을 나타내는 응답을 반환합니다.
     * @throws BadRequestException 요청이 잘못된 경우 예외를 발생시킵니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     */
    @PostMapping(value = "/api/v1/feedbacks", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "피드백/문의사항 작성", description = "사용자가 피드백/문의사항을 작성하고 등록합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "피드백 저장 성공") }
    )
    public ResponseEntity<ApiResult<CreateFeedbackResponse>> createFeedback(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateFeedbackRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        CreateFeedbackResponse response = feedbackService.createFeedback(request, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 저장 성공", response));
    }

    /**
     * 사용자가 본인의 특정 피드백을 조회하는 API입니다.
     *
     * <p>이 메소드는 사용자가 제출한 특정 피드백이나 문의사항의 상세 정보를 조회합니다.
     * 사용자는 이 메소드를 통해 자신이 제출한 피드백의 상태나 내용을 확인하고, 필요한 경우 수정할 수 있습니다.</p>
     *
     * @param token 사용자 인증을 위한 Bearer 토큰. 요청 헤더에 포함되어야 합니다.
     * @param feedbackNo 조회하고자 하는 피드백의 고유 번호. URL 경로에 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult<GetFeedbackResponse> 타입의 응답을 반환합니다.
     *         응답 본문에는 피드백 조회 성공 메시지와 함께, 요청받은 피드백 번호에 해당하는 피드백의 상세 정보가 포함됩니다.
     * @throws AccessDeniedException 사용자 본인이 아닌 다른 사용자의 피드백을 조회하려고 할 때 발생하는 예외입니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     */
    @GetMapping(value = "/api/v1/feedbacks/{feedbackNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "특정 피드백 상세 조회",
            description = "사용자가 제출한 특정 피드백이나 문의사항의 상세 정보를 조회합니다. 사용자가 자신이 제출한 피드백의 상태나 내용을 확인하고 수정할 수 있습니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "feedbackNo", description = "조회하고자 하는 피드백의 고유 번호", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "피드백 조회 성공") }
    )
    public ResponseEntity<ApiResult<GetFeedbackResponse>> getFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        GetFeedbackResponse response = feedbackService.getFeedback(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 조회 성공", response));
    }

    /**
     * 사용자가 작성한 모든 피드백 및 문의사항 리스트를 조회하는 API입니다.
     *
     * <p>이 메서드는 사용자가 자신이 작성한 피드백과 문의사항의 리스트를 페이지 형식으로 조회할 수 있게 합니다.
     * 사용자는 본인이 작성한 피드백의 전체 리스트를 확인할 수 있으며, 페이지 번호, 페이지 당 항목 수, 정렬 순서를 지정하여 조회할 수 있습니다.</p>
     *
     * @param token 사용자 인증을 위한 Bearer 토큰. 헤더에 포함되어야 합니다. 필수입니다.
     * @param page 조회하고자 하는 페이지 번호. 기본값은 0입니다.
     * @param size 한 페이지당 표시할 항목의 수. 기본값은 10입니다.
     * @param sort 항목의 정렬 순서. 'asc' 또는 'desc'를 지정할 수 있으며, 기본값은 'desc'입니다.
     *             정렬은 생성 날짜(createDate)를 기준으로 합니다.
     * @return {@code ResponseEntity<ApiResult<Page<GetFeedbackResponse>>>} 형태로,
     *         조회 성공 시 사용자가 작성한 피드백의 페이지 정보와 함께 "피드백 페이지 조회 성공" 메시지를 담아 반환합니다.
     *         반환되는 {@code ApiResult} 객체에는 조회된 피드백 리스트가 {@code Page<GetFeedbackResponse>} 형태로 포함됩니다.
     *
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     *          페이지 번호, 페이지 당 항목 수, 정렬 순서는 요청 파라미터를 통해 조정할 수 있으며,
     *          이들 파라미터는 각각 기본값을 가지고 있어, 요청 시 명시적으로 지정하지 않아도 됩니다.
     *          정렬 순서는 'asc' 또는 'desc' 중 하나를 선택하여 지정할 수 있으며, 정렬 기준은 생성 날짜입니다.
     */
    @GetMapping(value = "/api/v1/feedbacks", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "사용자가 작성한 피드백 리스트 조회", description = "사용자 본인이 작성한 전체 피드백/문의사항 리스트를 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "한 페이지당 항목 수"),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 순서 ('asc' 또는 'desc')")
            },
            responses = { @ApiResponse(responseCode = "200", description = "피드백 페이지 조회 성공") }
    )
    public ResponseEntity<ApiResult<FeedbackPageResponse<GetFeedbackResponse>>> getFeedbackScroll(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));

        FeedbackPageResponse<GetFeedbackResponse> response = feedbackService.getFeedbackScroll(pageable, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 페이지 조회 성공", response));
    }

    /**
     * 관리자가 특정 피드백을 조회하는 API입니다.
     *
     * <p>이 메소드는 관리자가 사용자가 제출한 특정 피드백이나 문의사항의 상세 정보를 조회합니다.
     * 관리자는 이 메소드를 통해 사용자가 제출한 피드백의 상태나 내용을 확인할 수 있습니다.</p>
     *
     * @param token 관리자 인증을 위한 Bearer 토큰. 요청 헤더에 포함되어야 합니다.
     * @param feedbackNo 조회하고자 하는 피드백의 고유 번호. URL 경로에 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult<GetFeedbackResponse> 타입의 응답을 반환합니다.
     *         응답 본문에는 피드백 조회 성공 메시지(관리자용)와 함께, 요청받은 피드백 번호에 해당하는 피드백의 상세 정보가 포함됩니다.
     * @HasAccess 어노테이션은 이 API가 관리자 권한을 필요로 함을 나타냅니다.
     * @throws AccessDeniedException 관리자 인증 정보가 유효하지 않을 때 발생하는 예외입니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     */
    @HasAccess
    @GetMapping(value = "/api/v1/feedbacks/admin/{feedbackNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "특정 피드백 상세 조회 (관리자)", description = "특정 피드백/문의사항을 관리자가 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "feedbackNo", description = "조회할 피드백의 고유 번호", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "피드백 조회 성공 : 관리자") }
    )
    public ResponseEntity<ApiResult<GetFeedbackResponse>> getFeedbackByAdmin(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        GetFeedbackResponse response = feedbackService.getFeedbackByAdmin(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 조회 성공 : 관리자", response));
    }

    /**
     * 관리자가 전체 피드백 리스트를 조회하는 API입니다.
     *
     * <p>이 메소드는 관리자가 전체 피드백/문의사항 리스트를 조회합니다.
     * 관리자는 이 메소드를 통해 사용자가 제출한 피드백의 상태나 내용을 확인할 수 있습니다.</p>
     *
     * @param token 관리자 인증을 위한 Bearer 토큰. 요청 헤더에 포함되어야 합니다.
     * @param page 조회하고자 하는 페이지 번호. 기본값은 0입니다.
     * @param size 한 페이지당 표시할 항목의 수. 기본값은 10입니다.
     * @param sort 항목의 정렬 순서. 'asc' 또는 'desc'를 지정할 수 있으며, 기본값은 'desc'입니다.
     *            정렬은 생성 날짜(createDate)를 기준으로 합니다.
     * @return {@code ResponseEntity<ApiResult<Page<GetFeedbackResponse>>} 형태로,
     *          조회 성공 시 전체 피드백 리스트와 함께 "피드백 리스트 조회 성공" 메시지를 담아 반환합니다.
     * @HasAccess 어노테이션은 이 API가 관리자 권한을 필요로 함을 나타냅니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     *          페이지 번호, 페이지 당 항목 수, 정렬 순서는 요청 파라미터를 통해 조정할 수 있으며,
     *          이들 파라미터는 각각 기본값을 가지고 있어, 요청 시 명시적으로 지정하지 않아도 됩니다.
     *          정렬 순서는 'asc' 또는 'desc' 중 하나를 선택하여 지정할 수 있으며, 정렬 기준은 생성 날짜입니다.
     */
    @HasAccess
    @GetMapping(value = "/api/v1/feedbacks/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "전체 피드백 리스트 조회 (관리자)", description = "관리자가 전체 피드백/문의사항 리스트를 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "한 페이지당 항목 수"),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 순서 ('asc' 또는 'desc')"),
                    @Parameter(in = ParameterIn.QUERY, name = "reply", description = "답변 여부 (true 또는 false, 선택적)")
            },
            responses = { @ApiResponse(responseCode = "200", description = "피드백 리스트 조회 성공 : 관리자") }
    )
    public ResponseEntity<ApiResult<FeedbackPageResponse<GetFeedbackResponse>>> getFeedbackScrollByAdmin(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc") String sort,
            @RequestParam(required = false) Boolean reply
    ) {
        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(sort), "createDate"));

        FeedbackPageResponse<GetFeedbackResponse> response;
        if (reply == null) {
            response = feedbackService.getFeedbackScrollByAdmin(pageable, memberNo);
        } else {
            response = feedbackService.getFeedbackScrollByAdminWithReply(pageable, memberNo, reply);
        }

        return ResponseEntity.ok(ApiResult.success("피드백 리스트 조회 성공 : 관리자", response));
    }


    /**
     * 사용자가 본인의 피드백을 삭제하는 API입니다.
     *
     * <p>이 메소드는 사용자가 제출한 특정 피드백이나 문의사항을 삭제합니다.
     * 사용자는 이 메소드를 통해 더 이상 필요하지 않은 피드백을 제거할 수 있습니다.
     * 삭제된 피드백은 복구할 수 없으므로, 사용 전 주의가 필요합니다.</p>
     *
     * @param token 사용자 인증을 위한 Bearer 토큰. 요청 헤더에 포함되어야 합니다.
     * @param feedbackNo 삭제하고자 하는 피드백의 고유 번호. URL 경로에 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult<DeleteFeedbackResponse> 타입의 응답을 반환합니다.
     *         응답 본문에는 피드백 삭제 성공 메시지와 함께, 삭제된 피드백의 정보가 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우, 또는 사용자 본인의 피드백이 아닐 경우 접근이 거부됩니다.
     */
    @DeleteMapping(value = "/api/v1/feedbacks/{feedbackNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "피드백 삭제", description = "특정 피드백/문의사항을 삭제합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "feedbackNo", description = "삭제할 피드백의 고유 번호", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "피드백 삭제 성공") }
    )
    public ResponseEntity<ApiResult<DeleteFeedbackResponse>> deleteFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo
    ) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        DeleteFeedbackResponse response = feedbackService.deleteFeedback(feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 삭제 성공", response));
    }

    /**
     * 사용자가 본인의 특정 피드백을 수정하는 API입니다.
     *
     * <p>이 메소드는 사용자가 제출한 특정 피드백이나 문의사항의 상세 정보를 수정합니다.
     * 사용자는 이 메소드를 통해 자신이 제출한 피드백의 내용을 수정할 수 있습니다.
     * 수정 가능한 내용은 피드백의 본문, 카테고리 등이 있으며, 수정 사항은 즉시 반영됩니다.</p>
     *
     * @param token 사용자 인증을 위한 Bearer 토큰. 요청 헤더에 포함되어야 합니다.
     * @param feedbackNo 수정하고자 하는 피드백의 고유 번호. URL 경로에 포함되어야 합니다.
     * @param request 피드백 수정 요청에 대한 세부 정보를 담은 객체. 요청 본문에 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult<UpdateFeedbackResponse> 타입의 응답을 반환합니다.
     *         응답 본문에는 피드백 수정 성공 메시지와 함께, 수정된 피드백의 상세 정보가 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 존재하지 않을 경우 접근이 거부됩니다.
     *          또한, 사용자는 본인의 피드백만 수정할 수 있습니다.
     * @throws BadRequestException 요청된 피드백 번호가 유효하지 않거나, 수정 요청 본문이 올바르지 않을 때 발생하는 예외입니다.
     */
    @PutMapping(value = "/api/v1/feedbacks/{feedbackNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "피드백 수정", description = "특정 피드백/문의사항을 수정합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "feedbackNo", description = "수정할 피드백의 고유 번호", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "피드백 수정 성공") }
    )
    public ResponseEntity<ApiResult<UpdateFeedbackResponse>> updateFeedback(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo,
            @RequestBody UpdateFeedbackRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        UpdateFeedbackResponse response = feedbackService.updateFeedback(request, feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 수정 성공", response));
    }

    /**
     * 관리자가 특정 피드백에 대한 답변을 작성하거나 수정하는 API입니다.
     *
     * <p>이 메서드를 통해 관리자는 사용자의 특정 피드백이나 문의사항에 대한 답변을 작성하거나 기존에 작성된 답변을 수정할 수 있습니다.
     * 관리자만 접근할 수 있는 기능으로, 관리자 인증을 위해 Bearer 토큰이 요청 헤더에 포함되어야 합니다.</p>
     *
     * @param token      사용자 인증을 위한 Bearer 토큰. 요청 헤더에 포함되어야 합니다.
     * @param feedbackNo 답변을 작성하거나 수정할 피드백의 고유 번호. URL 경로에 포함되어야 합니다.
     * @param request    피드백 답변 작성 또는 수정에 필요한 요청 본문 데이터.
     * @return ResponseEntity 객체를 통해 ApiResult<UpdateFeedbackReplyResponse> 타입의 응답을 반환합니다.
     *         응답 본문에는 피드백 답변 작성 또는 수정 성공 메시지와 함께, 답변 작성 또는 수정된 피드백의 상세 정보가 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 관리자가 아닐 경우 접근이 거부됩니다.
     * @throws BadRequestException 요청 본문이 올바르지 않거나 필요한 정보가 누락되었을 때 발생하는 예외입니다.
     */
    @HasAccess
    @PutMapping(value = "/api/v1/feedbacks/admin/{feedbackNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "피드백 답변 작성/수정 (관리자)", description = "관리자가 피드백 답변을 작성 또는 수정합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
                    @Parameter(in = ParameterIn.PATH, name = "feedbackNo", description = "답변을 작성할 피드백의 고유 번호", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "피드백 답변 작성/수정 완료 : 관리자") }
    )
    public ResponseEntity<ApiResult<UpdateFeedbackReplyResponse>> updateFeedbackReply(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("feedbackNo") Long feedbackNo,
            @RequestBody UpdateFeedbackReplyRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        UpdateFeedbackReplyResponse response = feedbackService.updateFeedbackReply(request, feedbackNo, memberNo);

        return ResponseEntity.ok(ApiResult.success("피드백 답변 작성/수정 완료 : 관리자", response));

    }
}
