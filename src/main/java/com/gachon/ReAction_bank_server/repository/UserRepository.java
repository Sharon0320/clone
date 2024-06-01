package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public Boolean existsByUserId(String userId);
}
