package com.example.CheatingStamp.model;

import com.example.CheatingStamp.dto.CalibrationRateRequestDto;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@Entity // DB 테이블 역할
public class User extends Timestamped {

    @Builder
    public User(String username, String name, String password, UserRole role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // 증가하는 ID 자동 생성
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "user_id")
    private Long id;

    // username으로 email 저장
    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<ExamUser> examUsers = new ArrayList<ExamUser>();

    @Column
    private int calibrationRate;  // 아이트래킹 결과

    public void updateCalibrationRate(CalibrationRateRequestDto requestDto) {
        this.calibrationRate = requestDto.getCalibrationRate();
    }
}