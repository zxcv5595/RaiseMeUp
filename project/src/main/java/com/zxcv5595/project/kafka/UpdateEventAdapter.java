package com.zxcv5595.project.kafka;


import com.zxcv5595.project.dto.UpdateCompletedMessage;
import com.zxcv5595.project.type.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateEventAdapter {

    private final ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate;

    public Mono<Void> send(UpdateCompletedMessage updateCompletedMessage) {
        return kafkaTemplate.send(KafkaTopic.UPDATE.getTopic(), updateCompletedMessage)
                .doOnSuccess(result -> {
                    log.info("Sent OrderCompletedMessage: {}", updateCompletedMessage);
                    // 성공적으로 전송되었을 때의 처리 작업
                })
                .doOnError(ex -> {
                    log.error("Error sending OrderCompletedMessage: {}", ex.getMessage(), ex);
                    // 전송 실패 시의 처리 작업
                })
                .then();
    }
}