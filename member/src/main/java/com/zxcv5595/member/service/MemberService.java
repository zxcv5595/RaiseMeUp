package com.zxcv5595.member.service;

import com.zxcv5595.member.domain.Member;
import com.zxcv5595.member.dto.Login;
import com.zxcv5595.member.dto.Signup;
import com.zxcv5595.member.exception.CustomException;
import com.zxcv5595.member.repository.MemberRepository;
import com.zxcv5595.member.security.TokenProvider;
import com.zxcv5595.member.type.ErrorCode;
import com.zxcv5595.member.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    /**
     *   1. 비밀번호 암호화
     *   2. 역할 부여 (ROLE_USER)
     *   3. Member 저장
     */
    public String signup(Signup.Request request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = new Member(request.getUsername(), encodedPassword, request.getPhone(),
                Role.ROLE_USER);
        memberRepository.save(member);

        return member.getUsername();
    }

    /**
     *   1. username으로 member 존재 여부 확인하기
     *   2. password 일치하는지 확인하기
     *   3. 토큰 발급
     */
    public String login(Login.Request user) {
        Member member = memberRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        validatePassword(user.getPassword(), member.getPassword());

        return tokenProvider.generateToken(member.getUsername(), member.getRole());
    }

    private void validatePassword(String loginPassword, String memberPassword) {
        if (!passwordEncoder.matches(loginPassword, memberPassword)) {
            throw new CustomException(ErrorCode.NOT_MATCHED_MEMBER);
        }
    }
}
