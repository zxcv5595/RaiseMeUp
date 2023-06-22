package com.zxcv5595.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Login {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        @NotEmpty(message = "Username cannot be empty")
        private String username;
        @NotEmpty(message = "Password cannot be empty")
        private String password;
    }

}
