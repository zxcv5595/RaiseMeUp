package com.zxcv5595.project.repository;


import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.domain.ProjectHistory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectHistoryRepository extends JpaRepository<ProjectHistory, Long> {

    Optional<ProjectHistory> findByProjectId(Project projectId);

    List<ProjectHistory> findByUpdatedAtBetweenAndFailedMessage(LocalDateTime startDay,
            LocalDateTime endDay, boolean failedMessage);

}
