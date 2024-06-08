package com.gachon.ReAction_bank_server.service;

import com.gachon.ReAction_bank_server.dto.statement.response.StatementInfo;
import com.gachon.ReAction_bank_server.dto.statement.response.StatementResponse;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.Statement;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.entity.transactionType;
import com.gachon.ReAction_bank_server.repository.AccountRepository;
import com.gachon.ReAction_bank_server.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gachon.ReAction_bank_server.entity.transactionType.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;
    private final AccountRepository accountRepository;
    public StatementResponse getUserStatements(User loginUser) {

        // 1. 로그인 유저의 계좌조회
        Account loginUserAccount = accountRepository
                .findByUser(loginUser)
                .orElseThrow(() -> new IllegalArgumentException("소유 중인 계좌가 없습니다!"));

        // 2. 이력 가져옴
        List<Statement> statements = statementRepository.getUserStatements(loginUserAccount);

        // 2-1. 빈 리스트일 경우 잔액만 반환
        if(statements.size() == 0)
            return new StatementResponse(List.of(), loginUserAccount.getBalance());

        // 3. 가공
        List<StatementInfo> result = statements.stream()
                .map(s -> {
                    LocalDateTime createdDate = s.getCreatedDate();
                    Account from = s.getFrom();
                    transactionType type = s.getType();
                    int amount = s.getAmount();

                    if (type == WITHDRAW || (type == TRANSFER && from.getId().equals(loginUserAccount.getId()))) {
                        amount *= -1;
                    }
                    return new StatementInfo(createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), amount, type.getExplanation());
                })
                .collect(Collectors.toList());

        return new StatementResponse(result, loginUserAccount.getBalance());
    }

}
