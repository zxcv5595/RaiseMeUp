package com.zxcv5595.project.repository;

import com.zxcv5595.project.domain.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByFailedMessage(boolean failedMessage);

}
