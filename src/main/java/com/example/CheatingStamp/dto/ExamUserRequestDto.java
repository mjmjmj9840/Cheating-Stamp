package com.example.CheatingStamp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExamUserRequestDto {
    private Long examId;
    private String username;
}
