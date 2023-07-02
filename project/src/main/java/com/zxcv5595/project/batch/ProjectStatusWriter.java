package com.zxcv5595.project.batch;

import com.zxcv5595.project.dto.FailureProjectMessage;
import com.zxcv5595.project.kafka.FailureProjectEventAdapter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectStatusWriter implements ItemWriter<Flux<FailureProjectMessage>> {

    private final FailureProjectEventAdapter failureProjectEventAdapter;
    private final MessageProcessor<FailureProjectMessage> messageProcessor;

    @Override
    public void write(Chunk<? extends Flux<FailureProjectMessage>> chunk) {

        log.info("Start writing failed projects");

        BlockingQueue<FailureProjectMessage> errorQueue = new LinkedBlockingQueue<>();

        for (Flux<FailureProjectMessage> failureProjectMessageFlux : chunk.getItems()) {
            failureProjectMessageFlux
                    .flatMap(failureProjectMessage -> failureProjectEventAdapter.send(
                                    failureProjectMessage)
                            .doOnError(ex -> {
                                log.error("Error sending failureProjectMessage: {}",
                                        ex.getMessage(), ex);
                                errorQueue.add(failureProjectMessage); // 실패한 원래 메시지를 에러 큐에 추가
                            })
                    )
                    .subscribe();
        }

        if (!errorQueue.isEmpty()) {
            messageProcessor.setErrorQueue(errorQueue);
        }
        log.info("Finished writing failureProjectMessage");
    }
}