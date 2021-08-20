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
    public Exam(String code, String title, LocalDateTime startTime, LocalDateTime endTime, String questions) {
        this.code = code;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.questions = questions;
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

    @NotNull
    private String questions;

    @ManyToMany
    @JoinTable(
            name = "user_exam",
            joinColumns = @JoinColumn(name = "user_id"),    // 현재 엔티티를 참조하는 외래 키
            inverseJoinColumns = @JoinColumn(name = "exam_id")) // 반대 방향 엔티티를 참조하는 외래 키
    private List<User> users = new ArrayList<User>();

    @OneToMany
    @JoinColumn(name = "EXAM_ID")
    private List<Video> videos = new ArrayList<Video>();

    public void addVideo(Video video) {
        videos.add(video);
        video.setExam(this);
    }
}
