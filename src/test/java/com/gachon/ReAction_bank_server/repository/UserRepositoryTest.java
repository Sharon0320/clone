package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.IntegrationTestSupport;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends IntegrationTestSupport {
   @Autowired
   private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("같은 ID가 저장되어 있는지 확인할 수 있다.")
    @Test
    void existsByAccountNum_green() {
        // given
        User u1 = User.of("test1", "id1", "pw");
        User u2 = User.of("test2", "id2", "pw");
        userRepository.saveAll(List.of(u1, u2));

        // when
        Boolean result = userRepository.existsByUserId("id1");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("저장된 ID가 없는 상황에서는 false를 반환한다.")
    @Test
    void existsByAccountNum_null() {
        // given

        // when
        Boolean result = userRepository.existsByUserId("id1");

        // then
        assertThat(result).isFalse();
    }
}