package com.zxcv5595.project.repository;

import com.zxcv5595.project.domain.FailedMessage;
import com.zxcv5595.project.domain.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedMessageRepository extends JpaRepository<FailedMessage, Long> {

    Optional<FailedMessage> findByProjectId(Project project);

    List<FailedMessage> findByFailure(boolean failure);
}
