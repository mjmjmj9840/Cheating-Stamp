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
public class Exam extends Timestamped {

    @Builder
    public Exam(String code, String title, LocalDateTime startTime, LocalDateTime endTime) {
        this.code = code;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(unique = true)
    @NotNull
    private String code;

    @NotNull
    private String title;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @OneToMany
    @JoinColumn(name = "EXAM_ID")
    private List<Question> questions = new ArrayList<Question>();

    public void addQuestion(Question question) {
        questions.add(question);
        question.setExam(this);
    }
}
