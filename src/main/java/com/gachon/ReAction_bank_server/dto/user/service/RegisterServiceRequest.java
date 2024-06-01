package com.gachon.ReAction_bank_server.dto.user.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterServiceRequest {
    private String userId;
    private String pw;
    private String name;
    private String accountNum;

    @Builder
    private RegisterServiceRequest(String userId, String pw, String name, String accountNum) {
        this.userId = userId;
        this.pw = pw;
        this.name = name;
        this.accountNum = accountNum;
    }
}
