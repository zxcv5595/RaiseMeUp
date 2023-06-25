package com.zxcv5595.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.RegisterProject;
import com.zxcv5595.project.exception.CustomException;
import com.zxcv5595.project.repository.ProjectRepository;
import com.zxcv5595.project.type.ErrorCode;
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

    @Test
    @DisplayName("프로젝트 시작일은 현재일 보다 이전일 수 없습니다.")
    public void registerProject_StartDateMustNotBeBeforeCurrentDate() {
        //given
        request.setStartDate(LocalDate.now().minusDays(1));
        request.setEndDate(LocalDate.now());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> projectService.registerProject(memberId, request));

        //then
        verifyNoInteractions(projectRepository);
        assertEquals(ErrorCode.INVALID_DATE.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("프로젝트 시작일은 마감 일보다 이전이어야 합니다.")
    public void registerProject_StartDateMustBeBeforeEndDate() {
        //given
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> projectService.registerProject(memberId, request));

        //then
        verifyNoInteractions(projectRepository);
        assertEquals(ErrorCode.INVALID_DATE.getMessage(), exception.getMessage());
    }
}