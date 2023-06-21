package com.zxcv5595.member.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcv5595.member.EnableMockMvc;
import com.zxcv5595.member.config.SecurityConfig;
import com.zxcv5595.member.dto.Login;
import com.zxcv5595.member.dto.Login.Request;
import com.zxcv5595.member.security.TokenProvider;
import com.zxcv5595.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class LoginControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;

    @MockBean
    private TokenProvider tokenProvider;

    private Login.Request loginUser;

    @BeforeEach
    public void setup() {

        // 가상의 사용자 정보
        loginUser = Login.Request.builder()
                .username("testUsername")
                .password("testPassword")
                .build();

    }

    @Test
    @DisplayName("로그인 성공")
    public void successfulLogin() throws Exception {

        //given
        String testToken = "testToken";

        when(memberService.login(any(Request.class))).thenReturn(testToken);

        //then
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andReturn();

        String responseToken = result.getResponse().getContentAsString();

        //verify
        assertEquals(testToken, responseToken);

    }
}