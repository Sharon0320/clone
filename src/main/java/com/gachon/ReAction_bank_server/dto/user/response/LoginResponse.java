package com.gachon.ReAction_bank_server.dto.user.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private String name;
    private String userId;
    private String accountNum;

    public LoginResponse(String name, String userId, String accountNum) {
        this.name = name;
        this.userId = userId;
        this.accountNum = accountNum;
    }
}
