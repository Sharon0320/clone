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

    private int balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Account(String accountNum, int balance, User user) {
        this.accountNum = accountNum;
        this.balance = balance;
        this.user = user;
    }
}