package com.gachon.ReAction_bank_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Table(name="users")
@Entity
@NoArgsConstructor
@Getter
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private String name;

    private String userId;

    private String pw;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder
    private User(String name, String userId, String pw, Account account) {
        this.name = name;
        this.userId = userId;
        this.pw = pw;
        this.account = account;
    }

    public static User of(String name, String userId, String pw, Account account){
        return User.builder()
                .name(name)
                .userId(userId)
                .pw(pw)
                .account(account)
                .build();
    }
}
