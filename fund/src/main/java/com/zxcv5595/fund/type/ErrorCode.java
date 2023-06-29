package com.zxcv5595.fund.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILED("유효하지 않는 값 입니다.",HttpStatus.BAD_REQUEST),
    INVALID_PROJECT("유효하지 않는 프로젝트 입니다.",HttpStatus.BAD_REQUEST),
    INVALID_PERMISSION("자신의 프로젝트는 후원할 수 없습니다.",HttpStatus.BAD_REQUEST);
    private final String message;
    private final HttpStatus status;


}

