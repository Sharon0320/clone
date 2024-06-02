package com.gachon.ReAction_bank_server.dto.account.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAccountResponse {
    private Long id;
    private String accountNum;
    private int balance;

    @Builder
    private UserAccountResponse(Long id, String accountNum, int balance) {
        this.id = id;
        this.accountNum = accountNum;
        this.balance = balance;
    }

    public static UserAccountResponse of(Long id, String accountNum, int balance){
        return UserAccountResponse.builder()
                .id(id)
                .accountNum(accountNum)
                .balance(balance)
                .build();
    }
}
