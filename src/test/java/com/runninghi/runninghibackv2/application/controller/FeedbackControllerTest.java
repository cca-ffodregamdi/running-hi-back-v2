package com.runninghi.runninghibackv2.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runninghi.runninghibackv2.application.controller.FeedbackController;
import com.runninghi.runninghibackv2.application.dto.feedback.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.application.dto.feedback.response.*;
import com.runninghi.runninghibackv2.application.service.FeedbackService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import com.runninghi.runninghibackv2.domain.enumtype.FeedbackCategory;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FeedbackController.class)
@MockBean(JpaMetamodelMappingContext.class)
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    String token;
    String title;
    String content;
    String nickname;
    String responseStatus;
    Long feedbackNo;
    Long memberNo;
    FeedbackCategory category;

    @BeforeEach
    void setUp() {
        token = "mocked-jwt-token";
        title = "title";
        content = "content";
        nickname = "nickname";
        responseStatus = "OK";
        feedbackNo = 1L;
        memberNo = 1L;
        category = FeedbackCategory.PROPOSAL;
    }

    private String getErrorMessage(ErrorCode errorCode) {
        return errorCode.getCode() + " : " + errorCode.getMessage();
    }

    private String getErrorStatus(ErrorCode errorCode) {
        return errorCode.getStatus().name();
    }

    @Test
    @DisplayName("피드백 생성")
    void testCreateFeedback() throws Exception {
        String responseMessage = "피드백 저장 성공";
        CreateFeedbackRequest request = new CreateFeedbackRequest(title, content, category.getValue());
        CreateFeedbackResponse response = new CreateFeedbackResponse(feedbackNo, title, content);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.createFeedback(request, memberNo)).thenReturn(response);

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/feedbacks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(responseStatus))
                .andExpect(jsonPath("$.message").value(responseMessage))
                .andExpect(jsonPath("$.data.feedbackNo").value(response.feedbackNo()))
                .andExpect(jsonPath("$.data.title").value(response.title()))
                .andExpect(jsonPath("$.data.content").value(response.content()));
    }

    @Test
    @DisplayName("피드백 생성 실패 - 유효하지않은 요청(제목 누락)")
    void testCreateFeedback_MissingRequiredTitle() throws Exception {
        String errorStatus = getErrorStatus(ErrorCode.BAD_REQUEST);
        String errorMessage = getErrorMessage(ErrorCode.BAD_REQUEST);
        CreateFeedbackRequest request = new CreateFeedbackRequest(null, content, category.getValue());

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.createFeedback(request, memberNo)).thenThrow(new BadRequestException());

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/feedbacks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(errorStatus))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("피드백 생성 실패 - 유효하지않은 요청(내용 누락)")
    void testCreateFeedback_MissingRequiredContent() throws Exception {
        CreateFeedbackRequest request = new CreateFeedbackRequest(title, null, category.getValue());

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.createFeedback(request, memberNo)).thenThrow(new BadRequestException());

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);


        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/feedbacks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.BAD_REQUEST)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.BAD_REQUEST)));
    }

    @Test
    @DisplayName("피드백 생성 실패 - 유효하지않은 요청(빈 입력값)")
    void testCreateFeedback_IllegalArgument() throws Exception {
        CreateFeedbackRequest request = new CreateFeedbackRequest("", content, category.getValue());

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.createFeedback(request, memberNo)).thenThrow(new BadRequestException());

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/feedbacks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.BAD_REQUEST)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.BAD_REQUEST)));
    }

    @Test
    @DisplayName("피드백 상세 조회")
    void testGetFeedback() throws Exception {
        String responseMessage = "피드백 조회 성공";
        GetFeedbackResponse response = new GetFeedbackResponse(1L, title, content, category, LocalDateTime.now(), LocalDateTime.now(), false, null, nickname);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.getFeedback(feedbackNo, memberNo)).thenReturn(response);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(responseStatus))
                .andExpect(jsonPath("$.message").value(responseMessage))
                .andExpect(jsonPath("$.data.title").value(response.title()))
                .andExpect(jsonPath("$.data.content").value(response.content()))
                .andExpect(jsonPath("$.data.category").value(response.category().toString()))
                .andExpect(jsonPath("$.data.createDate").exists())
                .andExpect(jsonPath("$.data.updateDate").exists())
                .andExpect(jsonPath("$.data.hasReply").value(response.hasReply()))
                .andExpect(jsonPath("$.data.reply").value(response.reply()))
                .andExpect(jsonPath("$.data.nickname").value(response.nickname()));
    }

    @Test
    @DisplayName("피드백 상세 조회 - 존재하지 않는 피드백")
    void testGetFeedback_NotFound() throws Exception {
        // Mockito를 사용하여 서비스 호출 시 FeedbackNotFoundException 예외를 던지도록 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.getFeedback(feedbackNo, memberNo)).thenThrow(new EntityNotFoundException());

        // 요청 수행 및 예외 상황에서의 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.ENTITY_NOT_FOUND)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.ENTITY_NOT_FOUND)));
    }

    @Test
    @DisplayName("본인의 피드백 리스트 조회")
    void testGetFeedbackScroll() throws Exception {
        String responseMessage = "피드백 페이지 조회 성공";
        Page<GetFeedbackResponse> pageResponse = new PageImpl<>(Collections.singletonList(
                new GetFeedbackResponse(1L, title, content, category,
                        LocalDateTime.now(), LocalDateTime.now(), false, null, nickname)
        ));

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(1L);
        when(feedbackService.getFeedbackScroll(any(Pageable.class), eq(1L))).thenReturn(pageResponse);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feedbacks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(responseStatus))
                .andExpect(jsonPath("$.message").value(responseMessage))
                .andExpect(jsonPath("$.data.content[0].title").value(pageResponse.getContent().get(0).title()))
                .andExpect(jsonPath("$.data.content[0].content").value(pageResponse.getContent().get(0).content()))
                .andExpect(jsonPath("$.data.content[0].category").value(pageResponse.getContent().get(0).category().toString()))
                .andExpect(jsonPath("$.data.content[0].hasReply").value(pageResponse.getContent().get(0).hasReply()))
                .andExpect(jsonPath("$.data.content[0].nickname").value(pageResponse.getContent().get(0).nickname()));
    }

    @Test
    @DisplayName("피드백 상세 조회 : 관리자")
    void testGetFeedbackByAdmin() throws Exception {
        String responseMessage = "피드백 조회 성공 : 관리자";
        GetFeedbackResponse response = new GetFeedbackResponse(1L, title, content, category, LocalDateTime.now(), LocalDateTime.now(), false, null, nickname);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.getFeedbackByAdmin(feedbackNo, memberNo)).thenReturn(response);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feedbacks/admin/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(responseStatus))
                .andExpect(jsonPath("$.message").value(responseMessage))
                .andExpect(jsonPath("$.data.title").value(response.title()))
                .andExpect(jsonPath("$.data.content").value(response.content()))
                .andExpect(jsonPath("$.data.category").value(response.category().toString()))
                .andExpect(jsonPath("$.data.createDate").exists())
                .andExpect(jsonPath("$.data.updateDate").exists())
                .andExpect(jsonPath("$.data.hasReply").value(response.hasReply()))
                .andExpect(jsonPath("$.data.reply").value(response.reply()))
                .andExpect(jsonPath("$.data.nickname").value(response.nickname()));
    }

    @Test
    @DisplayName("피드백 상세 조회 : 관리자 - 존재하지 않는 피드백")
    void testGetFeedbackByAdmin_NotFound() throws Exception {
        // Mockito를 사용하여 서비스 호출 시 FeedbackNotFoundException 예외를 던지도록 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.getFeedbackByAdmin(feedbackNo, memberNo)).thenThrow(new EntityNotFoundException());

        // 요청 수행 및 예외 상황에서의 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feedbacks/admin/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.ENTITY_NOT_FOUND)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.ENTITY_NOT_FOUND)));
    }


    @Test
    @DisplayName("피드백 리스트 조회 : 관리자")
    void testGetFeedbackScrollByAdmin() throws Exception {
        int page = 0;
        int size = 10;
        String sort = "desc";
        String responseMessage = "피드백 리스트 조회 성공 : 관리자";
        Page<GetFeedbackResponse> responsePage = new PageImpl<>(Collections.emptyList());

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.getFeedbackScrollByAdmin(any(Pageable.class), eq(1L))).thenReturn(responsePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feedbacks/admin")
                        .header("Authorization", token)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(responseStatus))
                .andExpect(jsonPath("$.message").value(responseMessage));
    }

    @Test
    @DisplayName("피드백 삭제 : success")
    void testDeleteFeedback() throws Exception {
        String responseMessage = "피드백 삭제 성공";
        DeleteFeedbackResponse response = new DeleteFeedbackResponse(feedbackNo);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.deleteFeedback(feedbackNo, memberNo)).thenReturn(response);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(responseStatus))
                .andExpect(jsonPath("$.message").value(responseMessage))
                .andExpect(jsonPath("$.data.feedbackNo").value(response.feedbackNo()));
    }

    @Test
    @DisplayName("피드백 삭제 : error - 본인 확인 실패")
    void testDeleteFeedback_FailOwnConfirmation() throws Exception {
        DeleteFeedbackResponse response = new DeleteFeedbackResponse(feedbackNo);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.deleteFeedback(feedbackNo, memberNo)).thenThrow(new AccessDeniedException("권한이 없습니다."));

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(response);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.ACCESS_DENIED)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.ACCESS_DENIED)));
    }

    @Test
    @DisplayName("피드백 수정 : success")
    void testUpdateFeedback() throws Exception {
        String updateTitle = "Updated Title";
        String updateContent = "Updated Content";
        String responseMessage = "피드백 수정 성공";
        FeedbackCategory updatedCategory = FeedbackCategory.WEBERROR;
        UpdateFeedbackRequest request = new UpdateFeedbackRequest(updateTitle, updateContent, updatedCategory.getValue());
        UpdateFeedbackResponse response = new UpdateFeedbackResponse(feedbackNo, updateTitle, updateContent, updatedCategory.getDescription());

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.updateFeedback(request, feedbackNo, memberNo)).thenReturn(response);

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(responseStatus))
                .andExpect(jsonPath("$.message").value(responseMessage))
                .andExpect(jsonPath("$.data.feedbackNo").value(response.feedbackNo()))
                .andExpect(jsonPath("$.data.title").value(response.title()))
                .andExpect(jsonPath("$.data.content").value(response.content()))
                .andExpect(jsonPath("$.data.category").value(response.category()));
    }

    @Test
    @DisplayName("피드백 수정 : error - 존재하지 않는 피드백")
    void testUpdateFeedback_NotFound() throws Exception {
        String updateTitle = "Updated Title";
        String updateContent = "Updated Content";
        FeedbackCategory updatedCategory = FeedbackCategory.WEBERROR;
        UpdateFeedbackRequest request = new UpdateFeedbackRequest(updateTitle, updateContent, updatedCategory.getValue());

        // Mockito를 사용하여 서비스 호출 시 FeedbackNotFoundException 예외를 던지도록 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.updateFeedback(request, feedbackNo, memberNo)).thenThrow(new EntityNotFoundException());

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 예외 상황에서의 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.ENTITY_NOT_FOUND)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.ENTITY_NOT_FOUND)));
    }

    @Test
    @DisplayName("피드백 수정 : error - 유효하지않은 요청(입력값 누락)")
    void testUpdateFeedback_MissingRequiredField() throws Exception {
        String updateTitle = "Updated Title";
        FeedbackCategory updatedCategory = FeedbackCategory.WEBERROR;
        UpdateFeedbackRequest request = new UpdateFeedbackRequest(updateTitle, null, updatedCategory.getValue());

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.updateFeedback(request, feedbackNo,memberNo)).thenThrow(new BadRequestException());

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.BAD_REQUEST)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.BAD_REQUEST)));
    }

    @Test
    @DisplayName("피드백 수정 : error - 유효하지않은 요청(잘못된 입력값)")
    void testUpdateFeedback_IllegalArgument() throws Exception {
        String updateTitle = "Updated Title";
        String updateContent = "";
        FeedbackCategory updatedCategory = FeedbackCategory.WEBERROR;
        UpdateFeedbackRequest request = new UpdateFeedbackRequest(updateTitle, updateContent, updatedCategory.getValue());

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.updateFeedback(request, feedbackNo, memberNo)).thenThrow(new BadRequestException());

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.BAD_REQUEST)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.BAD_REQUEST)));
    }


    @Test
    @DisplayName("피드백 답변 입력 (관리자) : success")
    void testUpdateFeedbackReply() throws Exception {
        String reply = "reply";
        boolean hasReply = true;
        String responseMessage = "피드백 답변 작성/수정 완료 : 관리자";
        UpdateFeedbackReplyRequest request = new UpdateFeedbackReplyRequest(reply);
        UpdateFeedbackReplyResponse response = new UpdateFeedbackReplyResponse(1L, title, content, category,
                LocalDateTime.now(), LocalDateTime.now(), hasReply, reply, nickname);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.updateFeedbackReply(request, feedbackNo, memberNo)).thenReturn(response);

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/feedbacks/admin/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(responseStatus))
                .andExpect(jsonPath("$.message").value(responseMessage))
                .andExpect(jsonPath("$.data.title").value(response.title()))
                .andExpect(jsonPath("$.data.content").value(response.content()))
                .andExpect(jsonPath("$.data.category").value(response.category().name()))
                .andExpect(jsonPath("$.data.createDate").exists())
                .andExpect(jsonPath("$.data.updateDate").exists())
                .andExpect(jsonPath("$.data.hasReply").value(response.hasReply()))
                .andExpect(jsonPath("$.data.reply").value(response.reply()))
                .andExpect(jsonPath("$.data.nickname").value(response.nickname()));
    }

    @Test
    @DisplayName("피드백 답변 입력 (관리자) : error - 유효하지않은 요청(잘못된 입력값)")
    void testUpdateFeedbackReply_InvalidRequest() throws Exception {
        String content = "";
        UpdateFeedbackReplyRequest request = new UpdateFeedbackReplyRequest(content);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.updateFeedbackReply(request, feedbackNo, memberNo)).thenThrow(new BadRequestException());

        // request를 Json 형태로 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/feedbacks/admin/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.status").value(getErrorStatus(ErrorCode.BAD_REQUEST)))
                .andExpect(jsonPath("$.message").value(getErrorMessage(ErrorCode.BAD_REQUEST)));
    }

}

