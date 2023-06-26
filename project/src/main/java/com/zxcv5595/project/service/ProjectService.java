package com.zxcv5595.project.service;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.RegisterProject;
import com.zxcv5595.project.dto.RegisterProject.Request;
import com.zxcv5595.project.repository.ProjectRepository;
import com.zxcv5595.project.type.ProjectStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public void registerProject(
            String memberId,
            RegisterProject.Request request) {

        Long memberIdAsLong = Long.parseLong(memberId);

        Project newProject = Request.toEntity(request);
        newProject.setMemberId(memberIdAsLong);
        newProject.setStatus(ProjectStatus.PROGRESS);

        projectRepository.save(newProject);
    }

}
