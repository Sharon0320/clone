package com.gachon.ReAction_bank_server.dto.statement.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StatementInfo {

    // xxxx-xx-xx 형태
    private String date;

    // 거래 유형에 따라 + / - 붙이기
    private int amount;

    // TransactionType의 explanation 들어감
    private String type;

    public StatementInfo(String date, int amount, String type) {
        this.date = date;
        this.amount = amount;
        this.type = type;
    }
}
