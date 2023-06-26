package com.zxcv5595.project.kafka;


import com.zxcv5595.project.dto.UpdateCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateEventAdapter {

    private static final String TOPIC = "update-topic";
    private static final String ORDER_MESSAGE_KEY = "update-key";
    private final ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate;

    public void send(UpdateCompletedMessage updateCompletedMessage) {
        Mono<SenderResult<Void>> mono = kafkaTemplate.send(TOPIC,
                ORDER_MESSAGE_KEY, updateCompletedMessage);

        mono.doOnSuccess(result -> {
            log.info("Sent OrderCompletedMessage: {}", updateCompletedMessage);
            // 성공적으로 전송되었을 때의 처리 작업
        }).doOnError(ex -> {
            log.error("Error sending OrderCompletedMessage: {}", ex.getMessage(), ex);
            // 전송 실패 시의 처리 작업
        }).subscribe(); // subscribe를 호출하여 실제로 메시지를 전송합니다.
    }
}