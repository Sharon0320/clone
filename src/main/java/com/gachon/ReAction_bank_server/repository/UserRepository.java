package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * parameter로 들어온 userId가 이미 있는 지 검사하는 역할
     *     - 회원가입 시 사용
     *
     * @param userId
     * @return Boolean
     */
    public Boolean existsByUserId(String userId);

    /**
     * parameter로 들어온
     * @param userId
     * @return User
     */
    public Optional<User> findByuserId(String userId);
}
