package com.zxcv5595.project.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {
    UPDATE("update-topic"),
    FAILURE_PROJECT("failure-project-topic");

    private final String topic;
}
