package com.example.CheatingStamp.model;

import com.sun.istack.NotNull;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import sun.tools.jconsole.JConsole;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

}
