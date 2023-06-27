package com.zxcv5595.project.batch;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectReader implements ItemReader<List<Project>> {

    private final ProjectRepository projectRepository;
    private boolean hasMoreProjects = true;

    @Override
    public List<Project> read() {
        if (!hasMoreProjects) {
            return null; // 프로젝트가 더 이상 없을 경우 null을 반환하여 배치 작업 종료
        }

        log.info("Start reading projects from project history");

        List<Project> projects = projectRepository.findByFailedMessage(true);

        for (Project projectHistory : projects) {
            projectHistory.setFailedMessage(false);
        }
        projectRepository.saveAll(projects);

        log.info("Finished processing project history. Retrieved {} project histories",
                projects.size());

        setHasMoreProjects(false); // 더 이상 프로젝트가 없음을 표시

        return projects;
    }

    public void setHasMoreProjects(boolean hasMoreProjects) {
        this.hasMoreProjects = hasMoreProjects;
    }
}

