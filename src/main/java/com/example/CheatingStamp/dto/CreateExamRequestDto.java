package com.example.CheatingStamp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class CreateExamRequestDto {
    // code : ExamService에서 생성
    private String title;
    // String -> LocalDateTime : ExamService에서 변환
    private String startTime;
    private String endTime;
    // jsonArray를 String으로 저장
    // ex) [{"1":"첫번째 문제"},{"2":"두번째 문제"}]
    private String questions;
}
