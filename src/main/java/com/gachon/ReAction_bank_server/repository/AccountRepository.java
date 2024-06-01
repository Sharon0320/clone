package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 이미 계좌번호가 존재한다면 true return
    public Boolean existsByAccountNum(String accountNum);
}
