package com.example.CheatingStamp.repository;

import com.example.CheatingStamp.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByCode(String code); // 감독관 연결 시 사용
    List<Exam> findAllByManagerId(Long managerId);
}