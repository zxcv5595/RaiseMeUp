package com.zxcv5595.project.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILED("유효하지 않는 값 입니다.",HttpStatus.BAD_REQUEST),
    INVALID_DATE("유효하지 않은 날짜 입니다.",HttpStatus.BAD_REQUEST);
    private final String message;
    private final HttpStatus status;


}

