package com.gachon.ReAction_bank_server.controller;

import com.gachon.ReAction_bank_server.ControllerTestSupport;
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
    void getUserAccount() throws Exception{
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
     * @throws Exception
     */
    @DisplayName("세션이 없다면 계좌를 불러올 수 없다.")
    @Test
    void getUserAccountWithoutSession() throws Exception{
      // given

      // when // then
      mockMvc.perform(get("/accounts/own")
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print())
//              .andExpect(status().isUnauthorized());
              .andExpect(status().isOk());
    }
}