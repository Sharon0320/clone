package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.IntegrationTestSupport;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class AccountRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAllInBatch();
    }

    @DisplayName("같은 계좌번호가 저장되어 있는지 확인할 수 있다.")
    @Test
    void existsByAccountNum_green() {
        // given
        Account a1 = new Account("111-222-3333", 0);
        Account a2 = new Account("111-222-3334", 10);
        accountRepository.saveAll(List.of(a1, a2));

        // when
        Boolean result = accountRepository.existsByAccountNum("111-222-3333");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("저장된 계좌번호가 없는 상황에서는 false를 반환한다.")
    @Test
    void existsByAccountNum_null() {
        // given

        // when
        Boolean result = accountRepository.existsByAccountNum("111-222-3333");

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("유저 정보를 이용해서 계좌를 가져올 수 있다.")
    @Test
    void findByUser() {
        // given
        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Account a1 = Account.of("111-222-3333", 0, users.get(0));
        Account a2 = Account.of("111-222-3334", 0, users.get(1));
        accountRepository.saveAll(List.of(a1, a2));

        // when
        Optional<Account> result = accountRepository.findByUser(u1);

        // then
        assertThat(result.get().getAccountNum()).isEqualTo("111-222-3333");
    }

    @DisplayName("계좌가 없을 경우 빈 객체가 반환된다.")
    @Test
    void findByUserWithEmptyAccount() {
        // given
        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        Optional<Account> result = accountRepository.findByUser(u1);

        // then
        assertThat(result.isEmpty()).isTrue();
    }
}