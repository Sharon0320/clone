package com.gachon.ReAction_bank_server.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.*;

@Table(name="users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"name", "userId"})
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
