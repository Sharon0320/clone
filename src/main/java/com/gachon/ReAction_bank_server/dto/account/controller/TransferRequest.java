package com.gachon.ReAction_bank_server.dto.account.controller;

import com.gachon.ReAction_bank_server.dto.account.service.TransferServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransferRequest {

    @NotBlank(message = "이체 대상 계좌번호는 필수입니다!")
    private String receiverAccountNum;

    @NotNull(message = "금액을 입력해주세요!")
    private Integer amount;

    @Builder
    private TransferRequest(String receiverAccountNum, Integer amount) {
        this.receiverAccountNum = receiverAccountNum;
        this.amount = amount;
    }

    public static TransferRequest of(String receiverAccountNum, Integer amount){
        return TransferRequest.builder()
                .receiverAccountNum(receiverAccountNum)
                .amount(amount)
                .build();
    }

    public TransferServiceRequest to(){
        return TransferServiceRequest.builder()
                .receiverAccountNum(receiverAccountNum)
                .amount(amount)
                .build();
    }
}
