package com.example.CheatingStamp.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "exam_id", "user_id" }))
public class ExamUser {
    @Builder
    public ExamUser(Exam exam, User user, String mobileUrl) {
        this.exam = exam;
        this.user = user;
        this.mobileUrl = mobileUrl;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    @NotNull
    private String mobileUrl;
}