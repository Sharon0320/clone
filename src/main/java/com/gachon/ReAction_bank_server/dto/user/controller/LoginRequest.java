package com.gachon.ReAction_bank_server.dto.user.controller;

import com.gachon.ReAction_bank_server.dto.user.service.LoginServiceRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty(message = "로그인 시 ID는 필수입니다")
    private String userId;
    @NotEmpty(message = "로그인 시 비밀번호는 필수입니다")
    private String pw;
    @Builder
    private LoginRequest(String userId, String pw) {
        this.userId = userId;
        this.pw = pw;
    }
    public static LoginRequest of(String userId, String pw){
        return LoginRequest.builder()
                .userId(userId)
                .pw(pw)
                .build();
    }
    public LoginServiceRequest to(){
        return LoginServiceRequest.of(this.userId, this.pw);
    }
}
