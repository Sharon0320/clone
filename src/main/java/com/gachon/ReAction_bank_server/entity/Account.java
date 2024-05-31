package com.gachon.ReAction_bank_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String accountNum;

    @Builder
    private Account(int id, String accountNum) {
        this.id = id;
        this.accountNum = accountNum;
    }
}