package com.gachon.ReAction_bank_server.service;

import com.gachon.ReAction_bank_server.IntegrationTestSupport;
import com.gachon.ReAction_bank_server.dto.account.response.UserAccountResponse;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.repository.AccountRepository;
import com.gachon.ReAction_bank_server.repository.UserRepository;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest extends IntegrationTestSupport {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("로그인 유저의 계좌를 가져올 수 있다.")
    @Test
    void getUserAccount() {
        // given
        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);

        Account a1 = Account.of("111-222-3333", 0, loginUser);
        Account a2 = Account.of("111-222-3334", 0, users.get(1));
        List<Account> accounts = accountRepository.saveAll(List.of(a1, a2));
        Account loginUserAccount = accounts.get(0);

        // when
        UserAccountResponse res = accountService.getUserAccount(loginUser);

        // then
        assertThat(res)
                .extracting("id", "accountNum", "balance")
                .contains(loginUserAccount.getId(), loginUserAccount.getAccountNum(), loginUserAccount.getBalance());
    }

    @DisplayName("유저는 반드시 하나의 계좌를 소유해야 한다.")
    @Test
    void getUserAccountWithoutAccount() {
        // given
        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);

        // when // then
        assertThatThrownBy(() -> accountService.getUserAccount(loginUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소유 중인 계좌가 없습니다!");
        }
}