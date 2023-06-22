package com.zxcv5595.member.controller;

import com.zxcv5595.member.dto.Signup;
import com.zxcv5595.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MemberController {

    private final MemberService memberService;

    /**
     * 필요한 request (Signup.Request)
     * - username: 6자리 이상 20자리 이하
     * - password: 8자리 이상 20자리 이하
     * - phone: 하이픈 포함 (ex: 010-1234-1234)
     * --------------------------------------------
     * role 설정 (ROLE_USER)
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody Signup.Request request){

        String username = memberService.signup(request);

        return ResponseEntity.ok(username + "님 회원가입이 완료되었습니다.");
    }
}
