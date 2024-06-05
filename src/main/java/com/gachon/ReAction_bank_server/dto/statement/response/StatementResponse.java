package com.gachon.ReAction_bank_server.dto.statement.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StatementResponse {
    private List<StatementInfo> statementInfos;
    private int balance;

    public StatementResponse(List<StatementInfo> statementInfos, int balance) {
        this.statementInfos = statementInfos;
        this.balance = balance;
    }
}
