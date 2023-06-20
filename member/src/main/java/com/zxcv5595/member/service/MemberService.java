package com.zxcv5595.member.service;

import com.zxcv5595.member.domain.Member;
import com.zxcv5595.member.dto.Signup;
import com.zxcv5595.member.exception.CustomException;
import com.zxcv5595.member.repository.MemberRepository;
import com.zxcv5595.member.type.ErrorCode;
import com.zxcv5595.member.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 비밀번호 암호화 역할 부여 (ROLE_USER) Member 저장
     */
    public void signup(Signup.Request request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = new Member(request.getUsername(), encodedPassword, request.getPhone(),
                Role.ROLE_USER);
        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_MEMBER)
        );
    }
}
