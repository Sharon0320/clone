package com.gachon.ReAction_bank_server.dto.user.response;

import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import lombok.*;

@Getter
@NoArgsConstructor
public class RegisterResponse {
    private String name;
    private String accountNum;

    @Builder
    private RegisterResponse(User user, Account account) {
        this.name = user.getName();
        this.accountNum = account.getAccountNum();
    }

    public static RegisterResponse of(User user, Account account){
        return RegisterResponse.builder()
                .user(user)
                .account(account)
                .build();
    }
}
