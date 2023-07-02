package com.zxcv5595.project.kafka;


import com.zxcv5595.project.dto.FailureProjectMessage;
import com.zxcv5595.project.type.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FailureProjectEventAdapter {

    private final ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate;

    public Mono<Void> send(FailureProjectMessage failureProjectMessage) {
        return kafkaTemplate.send(KafkaTopic.FAILURE_PROJECT.getTopic(), failureProjectMessage)
                .doOnSuccess(result -> {
                    log.info("Sent OrderCompletedMessage: {}", failureProjectMessage);
                    // 성공적으로 전송되었을 때의 처리 작업
                })
                .then();
    }
}
