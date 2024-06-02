package com.gachon.ReAction_bank_server.controller;

import com.gachon.ReAction_bank_server.ControllerTestSupport;
import com.gachon.ReAction_bank_server.dto.user.controller.LoginRequest;
import com.gachon.ReAction_bank_server.dto.user.controller.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class UserControllerTest extends ControllerTestSupport {

    @DisplayName("회원가입할 수 있다.")
    @Test
    void register() throws Exception {
        // given
        RegisterRequest req = RegisterRequest.builder()
                .userId("userId")
                .pw("pw")
                .name("kim")
                .accountNum("111-222-3333")
                .build();

        // when // then
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));
    }

    @DisplayName("회원가입 시 ID를 입력해야 한다")
    @Test
    void registerWithEmptyUserId() throws Exception {
        // given
        RegisterRequest req = RegisterRequest.builder()
                .pw("pw")
                .name("kim")
                .accountNum("111-222-3333")
                .build();

        // when // then
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("로그인에 사용할 ID를 입력해주세요!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원가입 시 비밀번호를 입력해야 한다")
    @Test
    void registerWithEmptyPassword() throws Exception {
        // given
        RegisterRequest req = RegisterRequest.builder()
                .userId("userId")
                .name("kim")
                .accountNum("111-222-3333")
                .build();

        // when // then
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("로그인에 사용할 비밀번호를 입력해주세요!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원가입 시 이름을 입력해야 한다")
    @Test
    void registerWithEmptyName() throws Exception {
        // given
        RegisterRequest req = RegisterRequest.builder()
                .userId("userId")
                .pw("pw")
                .accountNum("111-222-3333")
                .build();

        // when // then
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이름은 필수입니다!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원가입 시 계좌번호를 입력해야 한다")
    @Test
    void registerWithEmptyAccountNum() throws Exception {
        // given
        RegisterRequest req = RegisterRequest.builder()
                .userId("userId")
                .pw("pw")
                .name("kim")
                .build();

        // when // then
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("계좌번호는 필수입니다!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원가입 시 양식에 맞는 계좌번호를 입력해야 한다.")
    @CsvSource(value = {"1", "1-2", "1-2-3", "111-2-3", "1-222-333", "1-2-3333"})
    @ParameterizedTest(name = "{0}")
    void registerWithInvalidAccountNum(String invalidAccountNum) throws Exception {
        // given
        RegisterRequest req = RegisterRequest.builder()
                .userId("userId")
                .pw("pw")
                .name("kim")
                .accountNum(invalidAccountNum)
                .build();

        // when // then
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("계좌번호 양식에 맞춰 입력해주세요!"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * When value in session.setAttribute(name, value) is null, it acts as removeAttribute()!
     * S
     * @throws Exception
     */
    @DisplayName("로그인할 수 있다.")
    @Test
    void login() throws Exception{
        // given
        LoginRequest req = LoginRequest.of("userId", "pw");

        // when // then
        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(request().sessionAttribute("loginUser", is(nullValue())));
    }

    @DisplayName("로그인 시 ID는 필수이다.")
    @Test
    void loginWithEmptyID() throws Exception{
        // given
        LoginRequest req = LoginRequest.builder()
                .pw("pw")
                .build();

        // when // then
        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("로그인 시 ID는 필수입니다"));
    }

    @DisplayName("로그인 시 비밀번호는 필수이다.")
    @Test
    void loginWithEmptyPW() throws Exception{
        // given
        LoginRequest req = LoginRequest.builder()
                .userId("id")
                .build();

        // when // then
        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("로그인 시 비밀번호는 필수입니다"));
    }
}