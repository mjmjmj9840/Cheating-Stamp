package com.example.CheatingStamp.dto;

import com.example.CheatingStamp.model.Exam;
import com.example.CheatingStamp.model.User;
import com.example.CheatingStamp.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExamUserRequestDto {
    private Exam exam;
    private User user;
}
