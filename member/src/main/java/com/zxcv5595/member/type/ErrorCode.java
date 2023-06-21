package com.zxcv5595.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_MEMBER("이미 존재하는 회원입니다.",HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED("유효하지 않는 값 입니다.",HttpStatus.BAD_REQUEST),
    NOT_MATCHED_MEMBER("ID 혹은 비밀번호를 잘못 입력하셨거나 등록되지 않은 ID 입니다.",HttpStatus.BAD_REQUEST),
    ACCESS_DENIED("접근 권한이 없습니다.",HttpStatus.BAD_REQUEST),
    NOT_EXIST_MEMBER("존재하지 않은 회원입니다.",HttpStatus.BAD_REQUEST);
    private final String message;
    private final HttpStatus status;


}

