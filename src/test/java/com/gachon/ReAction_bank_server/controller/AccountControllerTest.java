package com.gachon.ReAction_bank_server.controller;

import com.gachon.ReAction_bank_server.ControllerTestSupport;
import com.gachon.ReAction_bank_server.dto.account.controller.TransferRequest;
import com.gachon.ReAction_bank_server.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends ControllerTestSupport {

    @DisplayName("로그인 유저의 계좌를 불러올 수 있다.")
    @Test
    void getUserAccount() throws Exception {
        // given
        User loginUser = User.of("kim", "id", "pw");

        // when // then
        mockMvc.perform(get("/accounts/own")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginUser", loginUser))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));
    }

    /**
     * Session이 없기 때문에 인터셉터에 걸림, 401 반환
     *
     * @throws Exception
     */
    @DisplayName("세션이 없다면 계좌를 불러올 수 없다.")
    @Test
    void getUserAccountWithoutSession() throws Exception {
        // given

        // when // then
        mockMvc.perform(get("/accounts/own")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("송금할 수 있다.")
    @Test
    void transfer() throws Exception{
        // given
        TransferRequest req = TransferRequest.of("111-222-3333", 100);
        User loginUser = User.of("kim", "han", "123");

        // when // then
        mockMvc.perform(post("/accounts/1/transfer")
                .sessionAttr("loginUser", loginUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));
    }

    @DisplayName("로그인하지 않으면 송금할 수 없다.")
    @Test
    void transfer_noSession() throws Exception{
        // given
        TransferRequest req = TransferRequest.of("111-222-3333", 100);

        // when // then
        mockMvc.perform(post("/accounts/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value(HttpStatus.UNAUTHORIZED.name()));
    }

    @DisplayName("계좌번호 없이 송금할 수 없다.")
    @Test
    void transfer_withoutAccountNum() throws Exception{
        // given
        TransferRequest req = TransferRequest.builder()
                .amount(100)
                .build();

        User loginUser = User.of("kim", "han", "123");

        // when // then
        mockMvc.perform(post("/accounts/1/transfer")
                        .sessionAttr("loginUser", loginUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이체 대상 계좌번호는 필수입니다!"));
    }

    @DisplayName("이체금액 없이 송금할 수 없다.")
    @Test
    void transfer_withoutAmount() throws Exception{
        // given
        TransferRequest req = TransferRequest.builder()
                .receiverAccountNum("111-222-3333")
                .build();

        User loginUser = User.of("kim", "han", "123");

        // when // then
        mockMvc.perform(post("/accounts/1/transfer")
                        .sessionAttr("loginUser", loginUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("금액을 입력해주세요!"));
    }
}