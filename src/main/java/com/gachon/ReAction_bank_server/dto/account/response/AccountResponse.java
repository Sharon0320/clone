package com.gachon.ReAction_bank_server.dto.account.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountResponse {
    private int balance;

    @Builder
    private AccountResponse(int balance) {
        this.balance = balance;
    }

    public static AccountResponse of(int balance){
        return AccountResponse.builder()
                .balance(balance)
                .build();
    }
}
