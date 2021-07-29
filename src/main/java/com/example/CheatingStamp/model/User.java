package com.example.CheatingStamp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@Entity // DB 테이블 역할
public class User extends Timestamped {
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 증가하는 ID 자동 생성
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    // username으로 email 저장
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
}