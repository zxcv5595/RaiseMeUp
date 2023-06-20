package com.zxcv5595.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_MEMBER("이미 존재하는 회원입니다.",HttpStatus.BAD_REQUEST);
    private final String message;
    private final HttpStatus status;


}

