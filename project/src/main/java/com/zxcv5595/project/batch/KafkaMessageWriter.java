package com.zxcv5595.project.batch;

import com.zxcv5595.project.dto.KafkaMessage;
import com.zxcv5595.project.dto.UpdateCompletedMessage;
import com.zxcv5595.project.kafka.UpdateEventAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageWriter implements ItemWriter<Flux<KafkaMessage>> {

    private final UpdateEventAdapter updateEventAdapter;

    @Override
    public void write(Chunk<? extends Flux<KafkaMessage>> chunk) {
        log.info("Start writing Kafka messages");

        for (Flux<KafkaMessage> kafkaMessageFlux : chunk.getItems()) {
            kafkaMessageFlux.flatMap(kafkaMessage ->
                            updateEventAdapter.send((UpdateCompletedMessage) kafkaMessage.getValue()))
                    .doOnError(ex -> {
                        log.error("Error sending KafkaMessage: {}", ex.getMessage(), ex);
                        // 전송 실패 시의 처리 작업
                    }).subscribe();
        }

        log.info("Finished writing Kafka messages");
    }

}

