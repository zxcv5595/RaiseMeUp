package com.zxcv5595.project.dto;

import com.zxcv5595.project.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final ErrorCode errorCode;
    private final int status;
    private final String message;

}