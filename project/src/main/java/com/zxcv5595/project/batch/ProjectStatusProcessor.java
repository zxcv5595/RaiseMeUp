package com.zxcv5595.project.batch;


import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.FailureProjectMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectStatusProcessor implements ItemProcessor<List<Project>, Flux<FailureProjectMessage>> {

    @Override
    public Flux<FailureProjectMessage> process(List<Project> projects) {
        log.info("start process");
        return Flux.fromIterable(projects)
                .map(this::createFailureProjectMessage);
    }


    private FailureProjectMessage createFailureProjectMessage(Project project) {
        return new FailureProjectMessage(project.getId());
    }
}
