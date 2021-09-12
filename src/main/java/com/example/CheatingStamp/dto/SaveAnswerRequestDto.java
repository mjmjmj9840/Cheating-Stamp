package com.example.CheatingStamp.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class SaveAnswerRequestDto {
    private Long examId;
    private String answers; // ex) [{"1":"첫번째 문제"},{"2":"두번째 문제"}]
    private String timestamp;
}
