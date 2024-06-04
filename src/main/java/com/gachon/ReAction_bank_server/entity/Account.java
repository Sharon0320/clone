package com.gachon.ReAction_bank_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "accountNum", "balance"})
public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

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

    public static Account of(String accountNum, int balance, User user){
        return Account.builder()
                .accountNum(accountNum)
                .balance(balance)
                .user(user)
                .build();
    }

    public static Account create(String accountNum, User user){
        return Account.builder()
                .accountNum(accountNum)
                .balance(0)
                .user(user)
                .build();
    }

    public Account(String accountNum, int balance){
        this.accountNum = accountNum;
        this.balance = balance;
    }

    public static Account createTestAccount(String accountNum, int balance) {
        return Account.builder()
                .accountNum(accountNum)
                .balance(balance)
                .build();
    }

    private int deposit(int amount){
        return this.balance += amount;
    }

    private int withdraw(int amount){
        return this.balance -= amount;
    }

    public int transfer(Account receiverAccount, int amount) {
        this.balance -= amount;
        receiverAccount.deposit(amount);
        return this.balance;
    }
}