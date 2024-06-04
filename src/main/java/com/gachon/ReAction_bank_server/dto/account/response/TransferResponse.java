package com.gachon.ReAction_bank_server.dto.account.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransferResponse {
    private int balance;

    @Builder
    private TransferResponse(int balance) {
        this.balance = balance;
    }

    public static TransferResponse of(int balance){
        return TransferResponse.builder()
                .balance(balance)
                .build();
    }
}
