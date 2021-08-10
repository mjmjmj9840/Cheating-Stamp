package com.example.CheatingStamp.repository;

import com.example.CheatingStamp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
