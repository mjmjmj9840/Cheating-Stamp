package com.example.CheatingStamp.repository;

import com.example.CheatingStamp.model.Exam;
import com.example.CheatingStamp.model.ExamUser;
import com.example.CheatingStamp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamUserRepository extends JpaRepository<ExamUser, Long> {
    Optional<ExamUser> findByExamAndUser(Exam exam, User user);
    void deleteAllByExam_Id(Long exam_id);
}
