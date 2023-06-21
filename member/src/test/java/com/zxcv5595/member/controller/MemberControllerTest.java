package com.zxcv5595.member.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcv5595.member.EnableMockMvc;
import com.zxcv5595.member.config.SecurityConfig;
import com.zxcv5595.member.dto.ErrorResponse;
import com.zxcv5595.member.dto.Signup;
import com.zxcv5595.member.security.TokenProvider;
import com.zxcv5595.member.service.MemberService;
import com.zxcv5595.member.type.ErrorCode;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@WebMvcTest(controllers = MemberController.class)
@EnableMockMvc
@Import(SecurityConfig.class)
public class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;

    @MockBean
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("회원가입 성공")
    public void testSignup_Successful() throws Exception {
        // Given
        Signup.Request request = new Signup.Request("username", "password", "010-1234-1234");

        ArgumentCaptor<Signup.Request> captor = ArgumentCaptor.forClass(Signup.Request.class);
        when(memberService.signup(captor.capture())).thenReturn(
                request.getUsername());
        // Then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("username님 회원가입이 완료되었습니다."));

        // Verify
        verify(memberService, times(1)).signup(captor.capture());
    }

    @Test
    @DisplayName("회원가입-유효성검사")
    public void testSignup_Validation() throws Exception {
        // Given
        Signup.Request request = new Signup.Request("user", "pass", "1234");

        // When
        MvcResult result = mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse.class);

        List<String> expectedErrors = Arrays.asList(
                "Username must be between 6 and 20 characters",
                "Invalid phone number ex:010-1234-1234",
                "Password must be between 8 and 20 characters"
        );

        assertEquals(ErrorCode.VALIDATION_FAILED, errorResponse.getErrorCode());
        for (String expectedError : expectedErrors) {
            assertTrue(errorResponse.getMessage().contains(expectedError));
        }

    }


    @Test
    @DisplayName("회원가입-유효성검사(공백문자)")
    public void testSignup_Validation_NotEmpty() throws Exception {
        // Given
        Signup.Request request = new Signup.Request("", "", "");

        // When
        MvcResult result = mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, ErrorResponse.class);

        List<String> expectedErrors = Arrays.asList(
                "Username cannot be empty",
                "Password cannot be empty",
                "PhoneNumber cannot be empty",
                "Password must be between 8 and 20 characters",
                "Username must be between 6 and 20 characters",
                "Invalid phone number ex:010-1234-1234"
        );

        assertEquals(ErrorCode.VALIDATION_FAILED, errorResponse.getErrorCode());
        for (String expectedError : expectedErrors) {
            assertTrue(errorResponse.getMessage().contains(expectedError));
        }
    }


}