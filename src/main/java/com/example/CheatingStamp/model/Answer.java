package com.example.CheatingStamp.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@Entity // DB 테이블 역할
public class Answer extends Timestamped {

    @Builder
    public Answer(String answers, String timestamp, String username, Long examId) {
        this.answers = answers;
        this.timestamp = timestamp;
        this.username = username;
        this.examId = examId;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @NotNull
    private String answers;

    @NotNull
    private String timestamp;

    @NotNull
    private String username;

    @NotNull
    private Long examId;
}
