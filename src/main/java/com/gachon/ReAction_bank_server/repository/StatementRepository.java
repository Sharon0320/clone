package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {

    /**
     * 로그인 유저 관련 모든 거래내역 조회
     *     - from, to가 같을 경우 > 예금, 출금
     *     -           다를 경우 > 이체
     *
     * @param loginUserAccount
     * @return List<Statement>
     *     - 이런 거는 queryDsl 써서 하면 DTO로 간단하게 반환할 수 잇습니당
     *     - 거기에 예를 들어 예금만, 출금만, 이체만, 예금 + 출금만, 이렇게 조건 거는 건 동적 쿼리로 가능한데
     *       훨씬 간단하게 할 수 잇어요.
     */
    @Query("select s from Statement s " +
            "where s.from =:loginUserAccount or s.to =:loginUserAccount " +
            "ORDER BY s.createdDate desc")
    public List<Statement> getUserStatements(@Param("loginUserAccount") Account loginUserAccount);
}
