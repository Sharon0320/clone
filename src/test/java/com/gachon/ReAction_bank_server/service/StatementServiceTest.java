package com.gachon.ReAction_bank_server.service;

import com.gachon.ReAction_bank_server.IntegrationTestSupport;
import com.gachon.ReAction_bank_server.dto.statement.response.StatementResponse;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.Statement;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.entity.transactionType;
import com.gachon.ReAction_bank_server.repository.AccountRepository;
import com.gachon.ReAction_bank_server.repository.StatementRepository;
import com.gachon.ReAction_bank_server.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static com.gachon.ReAction_bank_server.entity.transactionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class StatementServiceTest extends IntegrationTestSupport {

    @Autowired
    private StatementService statementService;
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

    @Autowired
    private StatementRepository statementRepository;

    @DisplayName("유저의 계좌 내역을 불러올 수 있다.")
    @Test
    void getUserStatements() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 6, 5, 18, 0, 0);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, loginUser);
        Account a2 = Account.of("111-222-3334", 2000, notLoginUser);
        List<Account> accounts = accountRepository.saveAll(List.of(a1, a2));
        Account loginUserAccount = accounts.get(0);
        Account notLoginUserAccount = accounts.get(1);

        // 자신 계좌로 입금 (조회 대상)
        Statement s1 = createStatementForTest(loginUserAccount, loginUserAccount, 100, DEPOSIT);

        // 자신 계좌에서 출금 (조회 대상)
        Statement s2 = createStatementForTest(loginUserAccount, loginUserAccount, 200, WITHDRAW);

        // 자신 계좌에서 타 계좌로 이체 (조회 대상)
        Statement s3 = createStatementForTest(loginUserAccount, notLoginUserAccount, 300, TRANSFER);

        // 타 계좌에서 자신 계좌로 이체 (조회 대상)
        Statement s4 = createStatementForTest(notLoginUserAccount, loginUserAccount, 400, TRANSFER);

        // 타 계좌에서 타 계좌로 출금 (조회 대상 X)
        Statement s5 = createStatementForTest(notLoginUserAccount, notLoginUserAccount, 50000, WITHDRAW);

        List<Statement> statements = statementRepository.saveAll(List.of(s1, s2, s3, s4, s5));

        // Set createdDate, private field by ReflectionTestUtils!
        for(int i = 0; i <statements.size(); i++){
            ReflectionTestUtils.setField(statements.get(i), "createdDate", now.plusDays(i));
        }

        // Merge, Update Query will execute! (All entities are already in DB)
        statementRepository.saveAll(statements);

        // when
        StatementResponse userStatement = statementService.getUserStatements(loginUser);

        // then
        assertThat(userStatement.getStatementInfos()).as("거래 이력을 조회할 수 있다.").hasSize(4)
                .extracting("date", "amount", "type")
                .containsExactly(
                        tuple("2024-06-08",400, "이체"),
                        tuple("2024-06-07", -300, "이체"),
                        tuple("2024-06-06", -200, "출금"),
                        tuple("2024-06-05", 100, "입금")
                );

        assertThat(userStatement.getBalance()).as("계좌 잔액을 조회할 수 있다.").isEqualTo(1000);
    }

    @DisplayName("이력이 없을 경우 잔액만 반환된다.")
    @Test
    void getUserStatements_EmptyStatement() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 6, 5, 18, 0, 0);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, loginUser);
        Account a2 = Account.of("111-222-3334", 2000, notLoginUser);
        List<Account> accounts = accountRepository.saveAll(List.of(a1, a2));
        Account loginUserAccount = accounts.get(0);
        Account notLoginUserAccount = accounts.get(1);

        // when
        StatementResponse userStatement = statementService.getUserStatements(loginUser);

        // then
        assertThat(userStatement.getStatementInfos()).as("거래 이력을 조회할 수 있다.").hasSize(0);

        assertThat(userStatement.getBalance()).as("계좌 잔액을 조회할 수 있다.").isEqualTo(1000);
    }

    private static Statement createStatementForTest(Account from, Account to, int amount, transactionType type) {
        return Statement.builder()
                .from(from)
                .to(to)
                .amount(amount)
                .type(type)
                .build();
    }
}