package com.gachon.ReAction_bank_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Statement extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account from;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account to;

    private int amount;

    private transactionType type;

    @Builder
    private Statement(Account from, Account to, int amount, transactionType type) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.type = type;
    }
}
