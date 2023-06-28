package com.zxcv5595.project.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILED("유효하지 않는 값 입니다.",HttpStatus.BAD_REQUEST),
    NOT_EXIST_PROJECT("일치하는 프로젝트가 없습니다.",HttpStatus.BAD_REQUEST),
    NOT_EXIST_HISTORY("일치하는 기록이 없습니다.",HttpStatus.BAD_REQUEST),
    INVALID_PERMISSION("수정 권한이 없습니다.",HttpStatus.BAD_REQUEST);
    private final String message;
    private final HttpStatus status;


}

