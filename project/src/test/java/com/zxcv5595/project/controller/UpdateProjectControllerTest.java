package com.zxcv5595.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcv5595.project.EnableMockMvc;
import com.zxcv5595.project.dto.UpdateProject.Request;
import com.zxcv5595.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProjectController.class)
@EnableMockMvc
class UpdateProjectControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectService projectService;

    @Test
    public void testUpdateProject() throws Exception {
        // Arrange
        long memberId = 1L;
        long projectId = 123L;
        String description = "New description";

        Request request = Request.builder()
                .projectId(projectId)
                .description(description)
                .build();

        // Act & Assert
        mockMvc.perform(post("/v1/api/project/update")
                        .header("X-memberId", String.valueOf(memberId))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("프로젝트 업데이트에 성공하였습니다."));

        // Verify
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);

        verify(projectService).updateProject(eq(memberId), captor.capture());

        assertEquals(projectId, captor.getValue().getProjectId());
        assertEquals(description, captor.getValue().getDescription());
    }

}