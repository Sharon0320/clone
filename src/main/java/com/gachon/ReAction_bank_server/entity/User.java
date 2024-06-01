package com.gachon.ReAction_bank_server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Table(name="users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String userId;

    private String pw;

    @Builder
    private User(String name, String userId, String pw) {
        this.name = name;
        this.userId = userId;
        this.pw = pw;
    }

    public static User of(String name, String userId, String pw){
        return User.builder()
                .name(name)
                .userId(userId)
                .pw(pw)
                .build();
    }
}
