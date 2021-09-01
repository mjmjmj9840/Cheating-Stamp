package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.ExamUserRequestDto;
import com.example.CheatingStamp.model.Exam;
import com.example.CheatingStamp.model.ExamUser;
import com.example.CheatingStamp.model.User;
import com.example.CheatingStamp.model.UserRole;
import com.example.CheatingStamp.repository.ExamRepository;
import com.example.CheatingStamp.repository.ExamUserRepository;
import com.example.CheatingStamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExamUserService {
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final ExamUserRepository examUserRepository;

    public HashMap getExamUsers(Long examId) {
        HashMap<String, List> infoMap = new HashMap<String, List>();
        Exam exam = examRepository.findById(examId).get();

        // supervisors, testers
        List<ExamUser> examUsers = exam.getExamUsers();
        List<String> supervisors = new ArrayList<>();
        List<String> testers = new ArrayList<>();
        for (int i = 0; i < examUsers.size(); i++) {
            User user = examUsers.get(i).getUser();
            if (user.getRole() == UserRole.SUPERVISOR)
                supervisors.add(user.getUsername());
            else
                testers.add(user.getUsername());
        }

        infoMap.put("supervisors", supervisors);
        infoMap.put("testers", testers);

        return infoMap;
    }

    @Transactional
    public void deleteByExamIdAndUsername(Long examId, String username) {
        Exam exam = examRepository.findById(examId).get();
        User user = userRepository.findByUsername(username).get();
        ExamUser examUser = examUserRepository.findByExamAndUser(exam, user).get();

        exam.getExamUsers().remove(examUser);
        user.getExamUsers().remove(examUser);
        examUserRepository.delete(examUser);
    }

    @Transactional
    public void addByExamIdAndUsername(ExamUserRequestDto requestDto) {
        Exam exam = examRepository.findById(requestDto.getExamId()).get();
        User user = userRepository.findByUsername(requestDto.getUsername()).get();
        ExamUser examUser = new ExamUser(exam, user);

        exam.getExamUsers().add(examUser);
        user.getExamUsers().add(examUser);
        examUserRepository.save(examUser);
    }
}
