package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.ExamUserRequestDto;
import com.example.CheatingStamp.model.*;
import com.example.CheatingStamp.repository.ExamRepository;
import com.example.CheatingStamp.repository.ExamUserRepository;
import com.example.CheatingStamp.repository.UserRepository;
import com.example.CheatingStamp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExamUserService {
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
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

    public JSONArray getTestersInfo(Long examId) {
        Exam exam = examRepository.findById(examId).get();
        List<ExamUser> examUsers = exam.getExamUsers();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < examUsers.size(); i++) {
            User user = examUsers.get(i).getUser();
            if (user.getRole() != UserRole.SUPERVISOR) {
                JSONObject jsonObj = new JSONObject();

                jsonObj.put("name", user.getName());
                String username = user.getUsername();
                jsonObj.put("username", username);
                Optional<Video> video = videoRepository.findByTitleAndExam_Id(username, examId);
                if (video.isPresent()) {
                    jsonObj.put("watchingVideo", video.get().getId());
                }
                else {
                    jsonObj.put("watchingVideo", null);
                }
                jsonArray.put(jsonObj);
            }
        }

        return jsonArray;
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

    @Transactional
    public void deleteByExamIds(List<Long> examIds) {
        for (int i = 0; i < examIds.size(); i++) {
            examUserRepository.deleteAllByExam_Id(examIds.get(i));
        }
    }
}
