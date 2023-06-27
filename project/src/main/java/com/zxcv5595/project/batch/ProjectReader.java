package com.zxcv5595.project.batch;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.domain.ProjectHistory;
import com.zxcv5595.project.repository.ProjectHistoryRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectReader implements ItemReader<List<Project>> {

    private final ProjectHistoryRepository projectHistoryRepository;
    private boolean hasMoreProjects = true;

    @Override
    public List<Project> read() {
        if (!hasMoreProjects) {
            return null; // 프로젝트가 더 이상 없을 경우 null을 반환하여 배치 작업 종료
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
        log.info("Start reading projects from project history");

        List<ProjectHistory> projectHistories = projectHistoryRepository.findByUpdatedAtBetweenAndFailedMessage(
                startDay, endDay, true);

        for (ProjectHistory projectHistory : projectHistories) {
            projectHistory.setFailedMessage(false);
        }
        projectHistoryRepository.saveAll(projectHistories);

        log.info("Finished processing project history. Retrieved {} project histories",
                projectHistories.size());

        setHasMoreProjects(false); // 더 이상 프로젝트가 없음을 표시

        return projectHistories.stream().map(ProjectHistory::getProjectId)
                .collect(Collectors.toList());
    }

    public void setHasMoreProjects(boolean hasMoreProjects) {
        this.hasMoreProjects = hasMoreProjects;
    }
}

