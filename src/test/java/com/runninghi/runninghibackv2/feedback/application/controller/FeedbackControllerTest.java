package com.runninghi.runninghibackv2.feedback.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import com.runninghi.runninghibackv2.feedback.application.dto.request.CreateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackReplyRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.request.UpdateFeedbackRequest;
import com.runninghi.runninghibackv2.feedback.application.dto.response.*;
import com.runninghi.runninghibackv2.feedback.application.service.FeedbackService;
import com.runninghi.runninghibackv2.feedback.domain.aggregate.entity.FeedbackCategory;
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
    Long feedbackNo;
    Long memberNo;
    FeedbackCategory category;

    @BeforeEach
    void setUp() {
        token = "mocked-jwt-token";
        title = "title";
        content = "content";
        nickname = "nickname";
        feedbackNo = 1L;
        memberNo = 1L;
        category = FeedbackCategory.PROPOSAL;
    }

    @Test
    @DisplayName("피드백 생성")
    void testCreateFeedback() throws Exception {
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
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("피드백 저장 성공"))
                .andExpect(jsonPath("$.data.feedbackNo").value(response.feedbackNo()))
                .andExpect(jsonPath("$.data.title").value(response.title()))
                .andExpect(jsonPath("$.data.content").value(response.content()));
    }

    @Test
    @DisplayName("피드백 생성 실패 - 유효하지않은 요청(입력값 누락)")
    void testCreateFeedback_MissingRequiredField() throws Exception {
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
                .andExpect(jsonPath("$.status").value(ErrorCode.BAD_REQUEST.getStatus().name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getCode() + " : " + ErrorCode.BAD_REQUEST.getMessage()));
    }

    @Test
    @DisplayName("피드백 생성 실패 - 유효하지않은 요청(잘못된 입력값)")
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
                .andExpect(jsonPath("$.status").value(ErrorCode.BAD_REQUEST.getStatus().name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getCode() + " : " + ErrorCode.BAD_REQUEST.getMessage()));
    }

    @Test
    @DisplayName("피드백 상세 조회")
    void testGetFeedback() throws Exception {
        GetFeedbackResponse response = new GetFeedbackResponse(title, content, category, LocalDateTime.now(), LocalDateTime.now(), false, null, nickname);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.getFeedback(feedbackNo, memberNo)).thenReturn(response);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("피드백 조회 성공"))
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
                .andExpect(jsonPath("$.status").value(ErrorCode.ENTITY_NOT_FOUND.getStatus().name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ENTITY_NOT_FOUND.getCode() + " : " + ErrorCode.ENTITY_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("본인의 피드백 리스트 조회")
    void testGetFeedbackScroll() throws Exception {
        Page<GetFeedbackResponse> pageResponse = new PageImpl<>(Collections.singletonList(
                new GetFeedbackResponse(title, content, category,
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
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("피드백 페이지 조회 성공"))
                .andExpect(jsonPath("$.data.content[0].title").value(pageResponse.getContent().get(0).title()))
                .andExpect(jsonPath("$.data.content[0].content").value(pageResponse.getContent().get(0).content()))
                .andExpect(jsonPath("$.data.content[0].category").value(pageResponse.getContent().get(0).category().toString()))
                .andExpect(jsonPath("$.data.content[0].hasReply").value(pageResponse.getContent().get(0).hasReply()))
                .andExpect(jsonPath("$.data.content[0].nickname").value(pageResponse.getContent().get(0).nickname()));
    }

    @Test
    @DisplayName("피드백 상세 조회 : 관리자")
    void testGetFeedbackByAdmin() throws Exception {
        GetFeedbackResponse response = new GetFeedbackResponse(title, content, category, LocalDateTime.now(), LocalDateTime.now(), false, null, nickname);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.getFeedbackByAdmin(feedbackNo, memberNo)).thenReturn(response);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feedbacks/admin/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("피드백 조회 성공 : 관리자"))
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
                .andExpect(jsonPath("$.status").value(ErrorCode.ENTITY_NOT_FOUND.getStatus().name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ENTITY_NOT_FOUND.getCode() + " : " + ErrorCode.ENTITY_NOT_FOUND.getMessage()));
    }


    @Test
    @DisplayName("피드백 리스트 조회 : 관리자")
    void testGetFeedbackScrollByAdmin() throws Exception {
        int page = 0;
        int size = 10;
        String sort = "desc";
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
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("피드백 리스트 조회 성공 : 관리자"));
    }

    @Test
    @DisplayName("피드백 삭제 controller 테스트")
    void testDeleteFeedback() throws Exception {
        DeleteFeedbackResponse response = new DeleteFeedbackResponse(feedbackNo);

        // Mockito를 사용하여 서비스 호출 및 응답 객체 반환 설정
        when(jwtTokenProvider.getMemberNoFromToken(token)).thenReturn(memberNo);
        when(feedbackService.deleteFeedback(feedbackNo, memberNo)).thenReturn(response);

        // 요청 수행 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/feedbacks/" + feedbackNo)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("피드백 삭제 성공"))
                .andExpect(jsonPath("$.data.feedbackNo").value(response.feedbackNo()));
    }

    @Test
    @DisplayName("피드백 수정")
    void testUpdateFeedback() throws Exception {
        String updateTitle = "Updated Title";
        String updateContent = "Updated Content";
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
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("피드백 수정 성공"))
                .andExpect(jsonPath("$.data.feedbackNo").value(response.feedbackNo()))
                .andExpect(jsonPath("$.data.title").value(response.title()))
                .andExpect(jsonPath("$.data.content").value(response.content()))
                .andExpect(jsonPath("$.data.category").value(response.category()));
    }

    @Test
    @DisplayName("피드백 수정 실패 - 존재하지 않는 피드백")
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
                .andExpect(jsonPath("$.status").value(ErrorCode.ENTITY_NOT_FOUND.getStatus().name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ENTITY_NOT_FOUND.getCode() + " : " + ErrorCode.ENTITY_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("피드백 수정 실패 - 유효하지않은 요청(입력값 누락)")
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
                .andExpect(jsonPath("$.status").value(ErrorCode.BAD_REQUEST.getStatus().name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getCode() + " : " + ErrorCode.BAD_REQUEST.getMessage()));
    }

    @Test
    @DisplayName("피드백 수정 실패 - 유효하지않은 요청(잘못된 입력값)")
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
                .andExpect(jsonPath("$.status").value(ErrorCode.BAD_REQUEST.getStatus().name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getCode() + " : " + ErrorCode.BAD_REQUEST.getMessage()));
    }


    @Test
    @DisplayName("피드백 답변 입력 : 관리자")
    void testUpdateFeedbackReply() throws Exception {
        String reply = "reply";
        boolean hasReply = true;

        UpdateFeedbackReplyRequest request = new UpdateFeedbackReplyRequest(reply);
        UpdateFeedbackReplyResponse response = new UpdateFeedbackReplyResponse(title, content, category,
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
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("피드백 답변 작성/수정 완료 : 관리자"))
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
    @DisplayName("피드백 답변 입력 : 관리자 - 유효하지않은 요청")
    void testUpdateFeedbackReply_InvalidRequest() throws Exception {
        UpdateFeedbackReplyRequest request = new UpdateFeedbackReplyRequest("");

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
                .andExpect(jsonPath("$.status").value(ErrorCode.BAD_REQUEST.getStatus().name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getCode() + " : " + ErrorCode.BAD_REQUEST.getMessage()));
    }

}
