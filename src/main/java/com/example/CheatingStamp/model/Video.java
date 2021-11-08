package com.example.CheatingStamp.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Video extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @NotNull
    private String username;

    @Column(columnDefinition = "TEXT")
    private String filePath;

    @Column
    private boolean isMobile;

    @ManyToOne
    @JoinColumn(name = "EXAM_ID", insertable = false, updatable = false)
    private Exam exam;

    @Builder
    public Video(Long id, String username, String filePath, boolean isMobile) {
        this.id = id;
        this.username = username;
        this.filePath = filePath;
        this.isMobile = isMobile;
    }
}
