package com.zxcv5595.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.UpdateCompletedMessage;
import com.zxcv5595.project.dto.UpdateProject;
import com.zxcv5595.project.exception.CustomException;
import com.zxcv5595.project.kafka.UpdateEventAdapter;
import com.zxcv5595.project.repository.FailedMessageRepository;
import com.zxcv5595.project.repository.ProjectRepository;
import com.zxcv5595.project.type.ProjectStatus;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class UpdateProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UpdateEventAdapter updateEventAdapter;

    @Mock
    private FailedMessageRepository failedMessageRepository;

    @InjectMocks
    private ProjectService projectService;

    private UpdateProject.Request request;
    private Project project;

    @BeforeEach
    public void setUp() {
        request = UpdateProject.Request.builder()
                .description("New description")
                .build();

        project = Project.builder()
                .title("Title")
                .description("Old description")
                .goalAmount(1000L)
                .status(ProjectStatus.PROGRESS)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();

    }

    @Test
    @DisplayName("프로젝트 업데이트 - 성공")
    public void updateProject_Successful() {
        //given
        long memberId = 1L;
        long projectId = 123L;

        request.setProjectId(projectId);

        project.setMemberId(memberId);
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(updateEventAdapter.send(any(UpdateCompletedMessage.class))).thenReturn(Mono.empty());

        //when
        projectService.updateProject(memberId, request);

        //then
        verify(projectRepository, times(1)).findById(request.getProjectId());
        verify(updateEventAdapter, times(1)).send(any(UpdateCompletedMessage.class));
        verifyNoMoreInteractions(failedMessageRepository);

        assertEquals("New description", project.getDescription());
    }

    @Test
    @DisplayName("프로젝트 업데이트 - 권한없음")
    public void updateProject_InvalidPermission() {
        //given
        long memberId = 1L;
        long projectId = 123L;

        request.setProjectId(projectId);

        project.setMemberId(2L); // 멤버 아이디 불일치
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        //when
        assertThrows(CustomException.class, () -> projectService.updateProject(memberId, request));

        //then
        verify(projectRepository, times(1)).findById(projectId);
        verifyNoMoreInteractions(updateEventAdapter, failedMessageRepository);

        assertEquals("Old description", project.getDescription());
    }
}