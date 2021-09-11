package com.example.CheatingStamp.repository;

import com.example.CheatingStamp.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByTitleAndExam_Id(String title, Long exam_id);
}
