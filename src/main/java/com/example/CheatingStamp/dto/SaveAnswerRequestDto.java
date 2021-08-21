package com.example.CheatingStamp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaveAnswerRequestDto {
    // jsonArray를 String으로 저장
    // ex) [{"1":"첫번째 문제"},{"2":"두번째 문제"}]
    private String answers;
    private String timestamp;
}
