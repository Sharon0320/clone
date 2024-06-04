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

    public void deposit(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("입금 금액은 양수여야 합니다.");
        }
        if (amount%10 != 0) {
            throw new IllegalArgumentException("1원 단위는 입금이 불가능합니다.");
        }
        this.balance += amount;
    }

    public void withdraw(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("출금 금액은 양수여야 합니다.");
        }
        if (amount%10 != 0) {
            throw new IllegalArgumentException("1원 단위는 출금이 불가능합니다.");
        }
        this.balance -= amount;
    }

    public int transfer(Account receiverAccount, int amount) {
        this.balance -= amount;
        receiverAccount.deposit(amount);
        return this.balance;
    }
}