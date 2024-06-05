package com.gachon.ReAction_bank_server.service;

import com.gachon.ReAction_bank_server.IntegrationTestSupport;
import com.gachon.ReAction_bank_server.dto.account.response.TransferResponse;
import com.gachon.ReAction_bank_server.dto.account.response.UserAccountResponse;
import com.gachon.ReAction_bank_server.dto.account.service.TransferServiceRequest;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.Statement;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.repository.AccountRepository;
import com.gachon.ReAction_bank_server.repository.StatementRepository;
import com.gachon.ReAction_bank_server.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gachon.ReAction_bank_server.entity.transactionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountServiceTest extends IntegrationTestSupport {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatementRepository statementRepository;

    @AfterEach
    void tearDown() {
        statementRepository.deleteAllInBatch();
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

    @Transactional // To avoid LazyInitializationException when checking statement's loginUserAccount, receiverAccount!
    @DisplayName("로그인 유저의 계좌에서 타 계좌로 이체할 수 있다.")
    @Test
    void transfer() {
        // given
        TransferServiceRequest req =
                TransferServiceRequest.of("111-222-3334", 600);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User receiver = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, loginUser);
        Account a2 = Account.of("111-222-3334", 2000, receiver);
        accountRepository.saveAll(List.of(a1, a2));

        // when
        TransferResponse response = accountService.transfer(loginUser, req);

        // then
        List<Account> accounts = accountRepository.findAll();
        Account loginUserAccount = accounts.get(0);
        Account receiverAccount = accounts.get(1);

        Statement statement = statementRepository.findAll().get(0);

        assertThat(loginUserAccount.getBalance()).as("상대에게 600원 이체").isEqualTo(400);
        assertThat(receiverAccount.getBalance()).as("상대에게 600원 받음").isEqualTo(2600);
        assertThat(response.getBalance()).as("응답은 로그인 유저의 계좌 잔액").isEqualTo(400);
        assertThat(statement)
                .extracting("from", "to", "amount", "type")
                .containsExactly(loginUserAccount, receiverAccount, 600, TRANSFER);
    }

    @DisplayName("본인 계좌가 없을 경우 이체할 수 없다.")
    @Test
    void transfer_NotOwnAccount() {
        // given
        TransferServiceRequest req =
                TransferServiceRequest.of("111-222-3334", 600);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, null);
        Account a2 = Account.of("111-222-3334", 2000, notLoginUser);
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> accountService.transfer(loginUser, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소유 중인 계좌가 없습니다!");

    }

    @DisplayName("존재하지 않는 계좌로 송금할 수 없다.")
    @Test
    void transfer_NotTransferToNonExistAccount() {
        // given
        TransferServiceRequest req =
                TransferServiceRequest.of("999-888-7777", 600);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, loginUser);
        Account a2 = Account.of("111-222-3334", 2000, notLoginUser);
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> accountService.transfer(loginUser, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("송금하려는 계좌가 존재하지 않습니다!");

    }

    @DisplayName("본인 계좌로는 이체할 수 없다.")
    @Test
    void transfer_NotToOwnAccount() {
        TransferServiceRequest req =
                TransferServiceRequest.of("111-222-3333", 600);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, loginUser);
        Account a2 = Account.of("111-222-3334", 2000, notLoginUser);
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> accountService.transfer(loginUser, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인 계좌로는 이체할 수 없습니다!");
    }

    @DisplayName("일의 단위의 돈 (ex) 11원, 203원)은 송금할 수 없다.")
    @Test
    void transfer_NotZeroInDigit() {
        // given
        TransferServiceRequest req =
                TransferServiceRequest.of("111-222-3334", 401);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, loginUser);
        Account a2 = Account.of("111-222-3334", 2000, notLoginUser);
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> accountService.transfer(loginUser, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("일 원 단위의 돈은 송금할 수 없습니다!");
    }

    @DisplayName("송금 액수가 0 이하일 경우 송금할 수 없다.")
    @Test
    void transfer_NotMinusMoney() {
        // given
        TransferServiceRequest req =
                TransferServiceRequest.of("111-222-3334", -600);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, loginUser);
        Account a2 = Account.of("111-222-3334", 2000, notLoginUser);
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> accountService.transfer(loginUser, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0원 이하는 송금할 수 없습니다!");
    }

    @DisplayName("본인 잔액보다 많은 돈은 송금할 수 없다.")
    @Test
    void transfer_NotExceedAccountBalance() {
        // given
        TransferServiceRequest req =
                TransferServiceRequest.of("111-222-3334", 6000);

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User loginUser = users.get(0);
        User notLoginUser = users.get(1);

        Account a1 = Account.of("111-222-3333", 1000, loginUser);
        Account a2 = Account.of("111-222-3334", 2000, notLoginUser);
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> accountService.transfer(loginUser, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계좌 잔액보다 더 송금할 수 없습니다!");
    }


}