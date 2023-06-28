package com.zxcv5595.project.service;

import com.zxcv5595.project.domain.FailedMessage;
import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.RegisterProject;
import com.zxcv5595.project.dto.RegisterProject.Request;
import com.zxcv5595.project.dto.UpdateCompletedMessage;
import com.zxcv5595.project.dto.UpdateProject;
import com.zxcv5595.project.exception.CustomException;
import com.zxcv5595.project.kafka.UpdateEventAdapter;
import com.zxcv5595.project.repository.FailedMessageRepository;
import com.zxcv5595.project.repository.ProjectRepository;
import com.zxcv5595.project.type.ErrorCode;
import com.zxcv5595.project.type.ProjectStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FailedMessageRepository failedMessageRepository;
    private final UpdateEventAdapter updateEventAdapter;


    public void registerProject(
            long memberId,
            RegisterProject.Request request) {

        Project newProject = Request.toEntity(request);
        newProject.setMemberId(memberId);
        newProject.setStatus(ProjectStatus.PROGRESS);

        projectRepository.save(newProject);
    }

    public void updateProject(
            long memberId,
            UpdateProject.Request request
    ) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PROJECT));

        verifyPermission(memberId, project);

        project.setDescription(request.getDescription());

        projectRepository.save(project);

        updateEventAdapter.send(new UpdateCompletedMessage(project.getId(), project.getTitle()))
                .doOnError(ex -> {
                    log.error("Error sending OrderCompletedMessage: {}", ex.getMessage(), ex);
                    processFailedMessages(project); // 에러 발생 시 processFailedMessages 호출
                })
                .subscribe();

    }

    private void processFailedMessages(Project project) {

        FailedMessage failedMessage = failedMessageRepository.findByProjectId(project)
                .orElseGet(() -> failedMessageRepository.save(new FailedMessage(project, true)));

        failedMessage.setFailure(true);

        failedMessageRepository.save(failedMessage);
    }


    private void verifyPermission(long memberId, Project project) {
        if (project.getMemberId() != memberId) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
    }

}
