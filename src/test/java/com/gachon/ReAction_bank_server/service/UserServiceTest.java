package com.gachon.ReAction_bank_server.service;

import com.gachon.ReAction_bank_server.IntegrationTestSupport;
import com.gachon.ReAction_bank_server.dto.user.controller.RegisterRequest;
import com.gachon.ReAction_bank_server.dto.user.response.LoginResponse;
import com.gachon.ReAction_bank_server.dto.user.response.RegisterResponse;
import com.gachon.ReAction_bank_server.dto.user.service.LoginServiceRequest;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.repository.AccountRepository;
import com.gachon.ReAction_bank_server.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserService userService;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입할 수 있다.")
    @Test
    void register_green() {
        // given
        RegisterRequest req = RegisterRequest.of("id", "pw", "gachon", "111-222-3333");

        // when
        RegisterResponse response = userService.register(req.to());

        // then
        assertThat(response)
                .extracting("name", "accountNum")
                .containsExactly("gachon", "111-222-3333");
    }

    @DisplayName("회원가입 시 중복된 ID를 사용할 수 없다.")
    @Test
    void register_duplicateID() {
        // given
        RegisterRequest req = RegisterRequest.of("id", "pw", "gachon", "111-222-3333");

        User u1 = User.of("gachon", "id", "pw");
        User u2 = User.of("reaction", "han", "pw");
        userRepository.saveAll(List.of(u1, u2));

        // when // then
        assertThatThrownBy(() -> userService.register(req.to()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 사람이 같은 ID를 사용중입니다! 다른 ID를 입력해주세요!");
    }

    @DisplayName("회원가입 시 중복된 계좌번호를 사용할 수 없다.")
    @Test
    void register_duplicateAccountNum() {
        // given
        RegisterRequest req = RegisterRequest.of("id", "pw", "gachon", "111-222-3333");

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Account a1 = Account.of("111-222-3333", 0, users.get(0));
        Account a2 = Account.of("111-222-3334", 0, users.get(1));
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> userService.register(req.to()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 사람이 같은 계좌번호를 사용중입니다! 다른 계좌번호를 입력해주세요!");
    }

    @DisplayName("로그인할 수 있다.")
    @Test
    void login() {
        // given
        LoginServiceRequest req = LoginServiceRequest.of("id1", "pw");

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

//        Account a1 = Account.of("111-222-3333", 0, users.get(0));
//        Account a2 = Account.of("111-222-3334", 0, users.get(1));
//        accountRepository.saveAll(List.of(a1, a2));

        // when
        User user = userService.login(req);

        // then
        assertThat(user.getUserId()).isEqualTo("id1");
        assertThat(user.getPw()).isEqualTo("pw");
    }

    @DisplayName("ID가 다를 경우 로그인할 수 없다.")
    @Test
    void loginWithDifferentID() {
        // given
        LoginServiceRequest req = LoginServiceRequest.of("reaction", "pw");

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Account a1 = Account.of("111-222-3333", 0, users.get(0));
        Account a2 = Account.of("111-222-3334", 0, users.get(1));
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> userService.login(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID 또는 비밀번호가 잘못되었습니다!");
    }

    @DisplayName("비밀번호가 다를 경우 로그인할 수 없다.")
    @Test
    void loginWithDifferentPW() {
        // given
        LoginServiceRequest req = LoginServiceRequest.of("id1", "password");

        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Account a1 = Account.of("111-222-3333", 0, users.get(0));
        Account a2 = Account.of("111-222-3334", 0, users.get(1));
        accountRepository.saveAll(List.of(a1, a2));

        // when // then
        assertThatThrownBy(() -> userService.login(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID 또는 비밀번호가 잘못되었습니다!");
    }
}