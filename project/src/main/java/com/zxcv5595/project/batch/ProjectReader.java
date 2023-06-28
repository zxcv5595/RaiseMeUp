package com.zxcv5595.project.batch;

import com.zxcv5595.project.domain.FailedMessage;
import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.repository.FailedMessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectReader implements ItemReader<List<Project>> {

    private final FailedMessageRepository failedMessageRepository;
    private boolean executed = false;

    @Override
    public List<Project> read() {
        if (executed) {
            return null; // 이미 실행되었으므로 중지
        }

        log.info("Start reading projects from failed_message table");

        List<FailedMessage> failedMessageList = failedMessageRepository.findByFailure(true);

        if (failedMessageList.isEmpty()) {
            setExecuted(true);
            return null;
        }

        for (FailedMessage failedMessage : failedMessageList) {
            failedMessage.setFailure(false);
        }
        failedMessageRepository.saveAll(failedMessageList);

        log.info("Finished processing failed message. Retrieved {} failed messages",
                failedMessageList.size());

        setExecuted(true);

        return failedMessageList.stream().map(FailedMessage::getProjectId)
                .toList();
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}

