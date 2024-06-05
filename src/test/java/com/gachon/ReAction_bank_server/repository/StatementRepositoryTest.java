package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.IntegrationTestSupport;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.Statement;
import com.gachon.ReAction_bank_server.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.gachon.ReAction_bank_server.entity.transactionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class StatementRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        statementRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저의 거래내역을 가져올 수 있다.")
    @Test
    void findStatement() {
        // given
        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 0, loginUser);
        Account a2 = Account.of("111-222-3334", 0, notLoginUser);
        List<Account> accounts = accountRepository.saveAll(List.of(a1, a2));
        Account loginUserAccount = accounts.get(0);
        Account notLoginUserAccount = accounts.get(1);

        // 자신 계좌로 입금 (조회 대상)
        Statement s1 = Statement.of(loginUserAccount, loginUserAccount, 100, DEPOSIT);

        // 자신 계좌에서 출금 (조회 대상)
        Statement s2 = Statement.of(loginUserAccount, loginUserAccount, 200, WITHDRAW);

        // 자신 계좌에서 타 계좌로 이체 (조회 대상)
        Statement s3 = Statement.of(loginUserAccount, notLoginUserAccount, 300, TRANSFER);

        // 타 계좌에서 자신 계좌로 이체 (조회 대상)
        Statement s4 = Statement.of(notLoginUserAccount, loginUserAccount, 400, TRANSFER);

        // 타 계좌에서 타 계좌로 출금 (조회 대상 X)
        Statement s5 = Statement.of(notLoginUserAccount, notLoginUserAccount, 50000, WITHDRAW);
        statementRepository.saveAll(List.of(s1, s2, s3, s4, s5));

        // when
        List<Statement> statements = statementRepository.getUserStatements(loginUserAccount);

        // then
        assertThat(statements).hasSize(4)
                .extracting("amount", "type")
                .containsExactlyInAnyOrder(
                        tuple(100, DEPOSIT),
                        tuple(200, WITHDRAW),
                        tuple(300, TRANSFER),
                        tuple(400, TRANSFER)
                );
    }

    @DisplayName("거래내역이 없을 수 있다.")
    @Test
    void findStatement_EmptyStatement(){
        // given
        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 0, loginUser);
        Account a2 = Account.of("111-222-3334", 0, notLoginUser);
        List<Account> accounts = accountRepository.saveAll(List.of(a1, a2));
        Account loginUserAccount = accounts.get(0);
        Account notLoginUserAccount = accounts.get(1);

        // when
        List<Statement> statements = statementRepository.getUserStatements(loginUserAccount);

        // then
        assertThat(statements).hasSize(0);
    }
}