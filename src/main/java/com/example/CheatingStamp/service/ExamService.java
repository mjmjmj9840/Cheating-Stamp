package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.CreateExamRequestDto;
import com.example.CheatingStamp.model.Answer;
import com.example.CheatingStamp.model.Exam;
import com.example.CheatingStamp.model.Video;
import com.example.CheatingStamp.repository.AnswerRepository;
import com.example.CheatingStamp.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final AnswerRepository answerRepository;

    // String 타입으로 받아온 startTime, timeout 변수를 LocalDateTime 타입으로 변경
    public LocalDateTime StringToTime(String string) {
        DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime timeEdited = LocalDateTime.parse(string, fomatter1);
        return timeEdited;
    }

    // requestDto로부터 정보 받아와 객체 생성
    public Long createExam(CreateExamRequestDto requestDto, Long managerId) {
        String title = requestDto.getTitle();
        String code = UUID.randomUUID().toString().replace("-", "");    // 고유식별자(UUID) 생성
        LocalDateTime starTime = StringToTime(requestDto.getStartTime());
        LocalDateTime endTime = StringToTime(requestDto.getEndTime());
        String questions = requestDto.getQuestions();

        Exam exam = new Exam(code, title, starTime, endTime, questions, managerId);
        examRepository.save(exam);

        System.out.println(code);   // (FE) exam code 출력
        return exam.getId();
    }

    public Long getExamIdByCode(String code) {
        Optional<Exam> found = examRepository.findByCode(code);
        if (!found.isPresent()) {
            throw new NullPointerException("해당 코드의 시험이 존재하지 않습니다.");
        }
        return found.get().getId();
    }

    public HashMap getExamInfo(Long id) {
        HashMap<String, String> infoMap = new HashMap<String, String>();
        Exam exam = examRepository.getById(id);
        // examCode
        String examCode = exam.getCode();
        infoMap.put("examCode", examCode);
        // examTitle
        String examTitle = exam.getTitle();
        infoMap.put("examTitle", examTitle);
        // examStartTime
        String examStartTime = exam.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        infoMap.put("examStartTime", examStartTime);
        // examEndTime
        String examEndTime = exam.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        infoMap.put("examEndTime", examEndTime);
        // examTime
        String examTime = ChronoUnit.MINUTES.between(exam.getStartTime(), exam.getEndTime()) + "분";
        infoMap.put("examTime", examTime);
        // examQuestions
        String examQuestions = exam.getQuestions();
        infoMap.put("examQuestions", examQuestions);

        return infoMap;
    }

    public List<HashMap<String, String>> getExamByManagerId(Long managerId) {
        List<Exam> exams = examRepository.findAllByManagerId(managerId);

        List<HashMap<String, String>> examList = new ArrayList<>();
        for (int i = 0; i < exams.size(); i++) {
            Long examId = exams.get(i).getId();
            HashMap<String, String> exam = new HashMap<>();
            HashMap<String,String> infoMap = getExamInfo(examId);

            exam.put("examId", examId.toString());
            exam.put("examTitle", infoMap.get("examTitle"));
            exam.put("examStartTime", infoMap.get("examStartTime"));
            exam.put("examEndTime", infoMap.get("examEndTime"));
            examList.add(exam);
        }
        return examList;
    }

    public void deleteExamByExamIds(List<Long> examIds) {
        for (int i = 0; i < examIds.size(); i++) {
            // answer와 exam은 join column이 없기때문에 직접 삭제해줘야 함
            List<Answer> answers = answerRepository.findAllByExamId(examIds.get(i));
            for (int j = 0; j < answers.size(); j++) {
                answerRepository.delete(answers.get(j));
            }
            examRepository.deleteById(examIds.get(i));
        }
    }

    @Transactional
    public void addVideoByExamCode(Video video, String code) {
        Optional<Exam> found = examRepository.findByCode(code);
        if (!found.isPresent()) {
            throw new NullPointerException("해당 코드의 시험이 존재하지 않습니다.");
        }
        found.get().addVideo(video);
    }
}