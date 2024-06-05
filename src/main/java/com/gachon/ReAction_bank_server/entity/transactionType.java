package com.gachon.ReAction_bank_server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum transactionType {
    DEPOSIT("입금"),
    WITHDRAW("출금"),
    TRANSFER("이체");

    private final String explanation;
}
