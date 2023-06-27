package com.zxcv5595.project.batch;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.domain.ProjectHistory;
import com.zxcv5595.project.dto.KafkaMessage;
import com.zxcv5595.project.dto.UpdateCompletedMessage;
import com.zxcv5595.project.repository.ProjectHistoryRepository;
import com.zxcv5595.project.type.KafkaTopic;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectProcessor implements ItemProcessor<List<Project>, Flux<KafkaMessage>> {

    private final ProjectHistoryRepository projectHistoryRepository;

    @Override
    public Flux<KafkaMessage> process(List<Project> projects) {
        log.info("start process");
        return Flux.fromIterable(projects)
                .filter(this::hasDescriptionChanged)
                .map(this::createKafkaMessage);
    }

    private boolean hasDescriptionChanged(Project project) {
        ProjectHistory projectHistory = projectHistoryRepository.findByProjectId(project)
                .orElse(null);

        if (projectHistory == null) {
            return false; // 이전 기록이 없는 경우
        }

        return !Objects.equals(projectHistory.getDescription(), project.getDescription());
    }

    private KafkaMessage createKafkaMessage(Project project) {
        return new KafkaMessage(
                KafkaTopic.UPDATE.getTopic(),
                new UpdateCompletedMessage(project.getId(), project.getTitle()));
    }
}