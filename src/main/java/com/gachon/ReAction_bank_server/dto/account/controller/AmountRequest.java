package com.gachon.ReAction_bank_server.dto.account.controller;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AmountRequest {
    private int amount;


    @Builder
    private AmountRequest(int amount) {
        this.amount = amount;
    }
}