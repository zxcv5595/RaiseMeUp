package com.zxcv5595.project.batch;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.KafkaMessage;
import com.zxcv5595.project.dto.UpdateCompletedMessage;
import com.zxcv5595.project.type.KafkaTopic;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectProcessor implements ItemProcessor<List<Project>, Flux<KafkaMessage>> {

    @Override
    public Flux<KafkaMessage> process(List<Project> projects) {
        log.info("start process");
        return Flux.fromIterable(projects)
                .map(this::createKafkaMessage);
    }


    private KafkaMessage createKafkaMessage(Project project) {
        return new KafkaMessage(
                KafkaTopic.UPDATE.getTopic(),
                new UpdateCompletedMessage(project.getId(), project.getTitle()));
    }
}