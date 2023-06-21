package com.zxcv5595.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.zxcv5595.member.domain.Member;
import com.zxcv5595.member.dto.Login;
import com.zxcv5595.member.exception.CustomException;
import com.zxcv5595.member.repository.MemberRepository;
import com.zxcv5595.member.security.TokenProvider;
import com.zxcv5595.member.type.ErrorCode;
import com.zxcv5595.member.type.Role;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    private Login.Request loginUser;
    private Member member;

    @BeforeEach
    public void setup() {

        // 가상의 사용자 정보
        loginUser = Login.Request.builder()
                .username("testUsername")
                .password("testPassword")
                .build();

        // 가상의 멤버 정보
        member = Member.builder()
                .username("testUsername")
                .password("dGVzdFBhc3N3b3Jk")
                .phone("010-1234-1234")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    @DisplayName("로그인 성공")
    public void SuccessfulLogin() {


        //given
        when(memberRepository.findByUsername(loginUser.getUsername())).thenReturn(
                Optional.of(member));

        when(passwordEncoder.matches(loginUser.getPassword(), member.getPassword())).thenReturn(
                true);

        when(tokenProvider.generateToken(member.getUsername(), member.getRole())).thenReturn(
                "testToken");

        //when
        String token = memberService.login(loginUser);

        //then
        assertEquals("testToken", token);
    }

    @Test
    @DisplayName("존재하지 않은 유저")
    public void InvalidUsername() {

        //given
        when(memberRepository.findByUsername(loginUser.getUsername())).thenReturn(
                Optional.empty());

        // Act and Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> memberService.login(loginUser));

        assertEquals(ErrorCode.NOT_EXIST_MEMBER, exception.getErrorCode());
    }

    @Test
    @DisplayName("틀린 비밀번호")
    public void InvalidPassword() {

        //given
        when(memberRepository.findByUsername(loginUser.getUsername())).thenReturn(
                Optional.of(member));

        when(passwordEncoder.matches(loginUser.getPassword(), member.getPassword())).thenReturn(
                false);

        // Act and Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> memberService.login(loginUser));

        assertEquals(ErrorCode.NOT_MATCHED_MEMBER, exception.getErrorCode());
    }

}