package com.zxcv5595.project.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.RegisterProject;
import com.zxcv5595.project.repository.ProjectRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private RegisterProject.Request request;
    private String memberId;

    @BeforeEach
    public void setup() {
        request = RegisterProject.Request.builder()
                .title("Your Title")
                .description("Your Description")
                .goalAmount(1000L)
                .build();
        memberId = "1";
    }

    @Test
    @DisplayName("프로젝트 등록 성공")
    public void registerProject_Successful() {
        //given
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));

        //when
        projectService.registerProject(memberId, request);

        //then
        verify(projectRepository).save(any(Project.class));
    }

}