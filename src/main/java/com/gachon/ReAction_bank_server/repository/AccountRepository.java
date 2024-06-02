package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 이미 계좌번호가 존재한다면 true return
    public Boolean existsByAccountNum(String accountNum);

    // user와 매핑된 account 가져옴
    public Optional<Account> findByUser(User user);
}
