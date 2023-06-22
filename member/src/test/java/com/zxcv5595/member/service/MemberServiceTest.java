package com.zxcv5595.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.zxcv5595.member.domain.Member;
import com.zxcv5595.member.dto.Signup;
import com.zxcv5595.member.exception.CustomException;
import com.zxcv5595.member.repository.MemberRepository;
import com.zxcv5595.member.type.ErrorCode;
import com.zxcv5595.member.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Captor
    private ArgumentCaptor<Member> memberCaptor;

    @Test
    @DisplayName("회원가입 성공")
    public void testSignup_Successful() {
        // Given
        Signup.Request request = new Signup.Request("username", "password", "phone");

        when(memberRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        memberService.signup(request);

        // Then
        verify(memberRepository, times(1)).existsByUsername(request.getUsername());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        Mockito.verify(memberRepository, times(1)).save(memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();
        assertEquals(request.getUsername(), savedMember.getUsername());
        assertEquals("encodedPassword", savedMember.getPassword());
        assertEquals(request.getPhone(), savedMember.getPhone());
        assertEquals(Role.ROLE_USER, savedMember.getRole());
    }

    @Test
    @DisplayName("회원가입 - 이미 존재하는 회원")
    public void testSignup_AlreadyExistMember() {
        // Given
        Signup.Request request = new Signup.Request("username", "password", "phone");

        when(memberRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // Act and Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> memberService.signup(request));
        assertEquals(ErrorCode.ALREADY_EXIST_MEMBER, exception.getErrorCode());

        verify(memberRepository, times(1)).existsByUsername("username");
        verifyNoMoreInteractions(passwordEncoder, memberRepository); //  mock 객체들과 상호작용이 더 이상 없음

    }

}