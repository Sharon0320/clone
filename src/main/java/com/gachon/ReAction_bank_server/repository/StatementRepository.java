package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatementRepository extends JpaRepository<Statement, Long> {
}
