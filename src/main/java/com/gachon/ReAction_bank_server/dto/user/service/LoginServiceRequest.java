package com.gachon.ReAction_bank_server.dto.user.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginServiceRequest {
    private String userId;
    private String pw;

    @Builder
    private LoginServiceRequest(String userId, String pw) {
        this.userId = userId;
        this.pw = pw;
    }

    public static LoginServiceRequest of(String userId, String pw){
        return LoginServiceRequest.builder()
                .userId(userId)
                .pw(pw)
                .build();
    }
}
