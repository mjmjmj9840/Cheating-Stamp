package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.SaveAnswerRequestDto;
import com.example.CheatingStamp.model.Answer;
import com.example.CheatingStamp.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    // requestDto로부터 정보 받아와 객체 생성
    public Long createAnswer(SaveAnswerRequestDto requestDto) {
        String answers = requestDto.getAnswers();
        String timestamp = requestDto.getTimestamp();

        Answer answer = new Answer(answers, timestamp);
        answerRepository.save(answer);

        return answer.getId();
    }
}