package com.zxcv5595.project.batch;

import com.zxcv5595.project.kafka.KafkaAdapter;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProcessor<T> {

    private final KafkaAdapter<T> kafkaAdapter;
    private BlockingQueue<T> errorQueue;

    @Scheduled(cron = "0 */30 * * * *")
    public void processErrorQueue() {
        try {
            T errorMessage = errorQueue.peek();
            processErrorMessage(errorMessage);
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }

    private void processErrorMessage(T errorMessage) {
        kafkaAdapter.send(errorMessage)
                .doOnSuccess(result -> {
                    log.info("Sent to reply: {}", result);
                    errorQueue.poll();
                })
                .doOnError(ex -> log.error("Error sending to reply: {}", ex.getMessage(), ex))
                .subscribe();

    }

    public void setErrorQueue(BlockingQueue<T> errorQueue) {
        this.errorQueue = errorQueue;
    }
}