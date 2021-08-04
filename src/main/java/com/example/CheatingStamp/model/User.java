package com.example.CheatingStamp.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@Entity // DB 테이블 역할
public class User extends Timestamped {

    @Builder
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
    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;
    public void encodePassword(PasswordEncoder passwordEncoder) {   // 암호화
        this.password = passwordEncoder.encode(this.password);
    }

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @NotNull
    @Column
    private int calibrationRate = 0;  // 아이트래킹 결과
}