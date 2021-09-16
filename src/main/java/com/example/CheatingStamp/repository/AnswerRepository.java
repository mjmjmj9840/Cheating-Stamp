package com.example.CheatingStamp.repository;

import com.example.CheatingStamp.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByExamIdAndUsername(Long examId, String username);
    List<Answer> findAllByExamId(Long examId);
}