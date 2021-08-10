package com.example.CheatingStamp.model;

import com.example.CheatingStamp.repository.ExamRepository;
import com.example.CheatingStamp.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class QuestionTest {
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    @Test
    public void saveQuestion() {
        // given
        Question question = new Question();
        question.setNumber(1);
        question.setMultipleChoice(true);
        question.setText("다음 보기 중 맞는 것을 고르시오.");
        question.setAnswer("3");
        List<Choice> choices = new ArrayList<>();
        choices.add(new Choice(1, "보기1"));
        choices.add(new Choice(2, "보기2"));
        choices.add(new Choice(3, "보기3"));
        question.setChoices(choices);

        // when
        Question result = questionRepository.save(question);

        // then
        assertThat(result.getText()).isEqualTo(question.getText());
        assertThat(result.getAnswer()).isEqualTo(question.getAnswer());
        assertThat(result).isEqualTo(question);
    }

    @Transactional
    @Test
    public void addQuestion() {
        // given
        Exam exam = new Exam();
        exam.setCode("ssdfsdf2o3ir8ufsdfkjlksjdhf");
        exam.setTitle("시험 제목");
        LocalDateTime startTime = LocalDateTime.of(2021, 8, 15, 12, 30);
        LocalDateTime endTime = LocalDateTime.of(2021, 8, 15, 14, 0);
        exam.setStartTime(startTime);
        exam.setEndTime(endTime);
        examRepository.save(exam);

        Question question = new Question();
        question.setNumber(1);
        question.setMultipleChoice(true);
        question.setText("다음 보기 중 맞는 것을 고르시오.");
        question.setAnswer("3");
        List<Choice> choices = new ArrayList<>();
        choices.add(new Choice(1, "보기1"));
        choices.add(new Choice(2, "보기2"));
        choices.add(new Choice(3, "보기3"));
        question.setChoices(choices);
        questionRepository.save(question);

        // when
        Exam result = examRepository.findByCode("ssdfsdf2o3ir8ufsdfkjlksjdhf").get();
        result.addQuestion(question);

        // then
        assertThat(result).isEqualTo(question.getExam());
        assertThat(result.getQuestions().get(0)).isEqualTo(question);
    }


}