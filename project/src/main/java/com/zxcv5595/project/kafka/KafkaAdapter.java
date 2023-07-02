package com.zxcv5595.project.kafka;

import com.zxcv5595.project.type.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAdapter<T> {

    private final ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate;

    public Mono<Void> send(T kafkaMessage) {
        return kafkaTemplate.send(KafkaTopic.FAILURE_PROJECT.getTopic(), kafkaMessage)
                .then();
    }
}
