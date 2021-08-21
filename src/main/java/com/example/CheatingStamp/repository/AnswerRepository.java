package com.example.CheatingStamp.repository;

import com.example.CheatingStamp.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}