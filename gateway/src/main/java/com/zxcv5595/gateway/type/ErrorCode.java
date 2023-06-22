package com.zxcv5595.gateway.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN("Invalid Jwt token");
    private final String message;

    public static String is(ErrorCode errorCode) {
        return errorCode.message;
    }
}
