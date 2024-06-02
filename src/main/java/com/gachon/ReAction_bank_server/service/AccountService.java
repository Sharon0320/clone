package com.gachon.ReAction_bank_server.service;

import com.gachon.ReAction_bank_server.dto.account.response.UserAccountResponse;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    public UserAccountResponse getUserAccount(User loginUser) {
        Account account = accountRepository
                .findByUser(loginUser)
                .orElseThrow(() -> new IllegalArgumentException("소유 중인 계좌가 없습니다!"));

        return UserAccountResponse.of(account.getId(), account.getAccountNum(), account.getBalance());
    }
}
