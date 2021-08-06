package com.example.CheatingStamp.validator;

import com.example.CheatingStamp.service.ExamService;
import com.example.CheatingStamp.dto.CreateExamRequestDto;
import com.example.CheatingStamp.model.Exam;
import com.example.CheatingStamp.repository.ExamRepository;
import jdk.vm.ci.meta.Local;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ExamValidator implements Validator {

        private final ExamService examService;

        @Override
        public boolean supports(Class<?> clazz) {
            return CreateExamRequestDto.class.equals(clazz);
        }

        @Override
        public void validate(Object obj, Errors errors) {   // 시험 생성 페이지에서 사용, 날짜 유효한지 계산
            CreateExamRequestDto createExamRequestDto = (CreateExamRequestDto) obj;

            LocalDateTime startTime = examService.StringToTime(createExamRequestDto.getStartTime());
            LocalDateTime endTime = examService.StringToTime(createExamRequestDto.getEndTime());

            if(!startTime.isBefore(endTime)){   // startTime > endTime
                errors.rejectValue("endTime", "key","마감 시간이 시작 시간보다 빠릅니다.");
            }
        }

}