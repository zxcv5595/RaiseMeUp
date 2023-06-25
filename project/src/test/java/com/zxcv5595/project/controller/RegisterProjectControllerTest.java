package com.zxcv5595.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcv5595.project.EnableMockMvc;
import com.zxcv5595.project.dto.ErrorResponse;
import com.zxcv5595.project.dto.RegisterProject;
import com.zxcv5595.project.service.ProjectService;
import com.zxcv5595.project.type.ErrorCode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = ProjectController.class)
@EnableMockMvc
class RegisterProjectControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectService projectService;

    private RegisterProject.Request request;
    private String memberId;

    @BeforeEach
    public void setup() {

        request = RegisterProject.Request.builder()
                .title("Your Title")
                .description("Your Description")
                .goalAmount(1000L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();
        memberId = "1";
    }

    @Test
    @DisplayName("프로젝트 등록 성공")
    void registerProject_Successful() throws Exception {

        //given
        ArgumentCaptor<String> memberIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<RegisterProject.Request> requestCaptor = ArgumentCaptor.forClass(
                RegisterProject.Request.class);

        doNothing().when(projectService)
                .registerProject(memberIdCaptor.capture(), requestCaptor.capture());

        //when
        mockMvc.perform(post("/v1/api/project/register")
                        .header("X-memberId", memberId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("프로젝트 등록에 성공하였습니다."));

        //then
        verify(projectService).registerProject(memberIdCaptor.capture(), requestCaptor.capture());

        assertEquals(memberId, memberIdCaptor.getValue());
        assertEquals(request.getTitle(), requestCaptor.getValue().getTitle());

    }

    @Test
    @DisplayName("프로젝트 등록 - 유효성 검사")
    void registerProject_Validation() throws Exception {
        // Given
        RegisterProject.Request invalidRequest = RegisterProject.Request.builder()
                .title("")
                .description("")
                .goalAmount(-1L)
                .startDate(null)
                .endDate(null)
                .build();

        // When
        MvcResult result = mockMvc.perform(post("/v1/api/project/register")
                        .header("X-memberId", memberId)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse.class);

        List<String> expectedErrors = Arrays.asList(
                "Title cannot be empty",
                "Description cannot be empty",
                "Goal amount must be a positive or zero value",
                "Start date cannot be null",
                "End date cannot be null"
        );

        //then
        assertEquals(ErrorCode.VALIDATION_FAILED, errorResponse.getErrorCode());
        for (String expectedError : expectedErrors) {
            assertTrue(errorResponse.getMessage().contains(expectedError));
        }
    }

}