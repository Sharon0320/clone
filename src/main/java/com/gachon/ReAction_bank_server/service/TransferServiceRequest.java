package com.gachon.ReAction_bank_server.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransferServiceRequest {
    private String receiverAccountNum;
    private int amount;

    @Builder
    private TransferServiceRequest(String receiverAccountNum, int amount) {
        this.receiverAccountNum = receiverAccountNum;
        this.amount = amount;
    }

    public static TransferServiceRequest of(String receiverAccountNum, int amount){
        return TransferServiceRequest.builder()
                .receiverAccountNum(receiverAccountNum)
                .amount(amount)
                .build();
    }
}
