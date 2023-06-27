package com.zxcv5595.project.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {
    UPDATE("update-topic");

    private final String topic;
}
