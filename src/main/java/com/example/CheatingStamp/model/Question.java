package com.example.CheatingStamp.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor // 기본 생성자 생성
@Entity // DB 테이블 역할
public class Question extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @NotNull
    private int number; // 문제 번호

    @NotNull
    private boolean multipleChoice;  // true: 객관식, false: 주관식

    @NotNull
    private String text; // 문제 지문

    @NotNull
    private String answer;  // 문제 정답

    @OneToMany(cascade = CascadeType.ALL)
    private List<Choice> choices;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EXAM_ID", insertable = false, updatable = false)
    private Exam exam;
}

@Setter
@Getter
@NoArgsConstructor
@Entity
class Choice {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    private int number;  // 보기 번호
    private String text;  // 보기 내용

    public Choice(int number, String text) {
        this.number = number;
        this.text = text;
    }
}
