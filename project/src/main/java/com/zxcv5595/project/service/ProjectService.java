package com.zxcv5595.project.service;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.RegisterProject;
import com.zxcv5595.project.dto.RegisterProject.Request;
import com.zxcv5595.project.dto.UpdateCompletedMessage;
import com.zxcv5595.project.dto.UpdateProject;
import com.zxcv5595.project.exception.CustomException;
import com.zxcv5595.project.kafka.UpdateEventAdapter;
import com.zxcv5595.project.repository.ProjectRepository;
import com.zxcv5595.project.type.ErrorCode;
import com.zxcv5595.project.type.ProjectStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UpdateEventAdapter updateEventAdapter;

    public void registerProject(
            String memberId,
            RegisterProject.Request request) {

        Long memberIdAsLong = Long.parseLong(memberId);

        Project newProject = Request.toEntity(request);
        newProject.setMemberId(memberIdAsLong);
        newProject.setStatus(ProjectStatus.PROGRESS);

        projectRepository.save(newProject);
    }

    public void updateProject(
            String memberId,
            UpdateProject.Request request
    ) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PROJECT));

        verifyPermission(memberId, project);

        project.setDescription(request.getDescription());

        projectRepository.save(project);

        updateEventAdapter.send(new UpdateCompletedMessage(project.getId(),project.getTitle()));

    }

    private void verifyPermission(String memberId, Project project) {
        if(project.getMemberId() != Long.parseLong(memberId)){
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
    }

}
