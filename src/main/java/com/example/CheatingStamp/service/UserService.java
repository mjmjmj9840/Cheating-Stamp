package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.CalibrationRateRequestDto;
import com.example.CheatingStamp.dto.SignupRequestDto;
import com.example.CheatingStamp.model.Exam;
import com.example.CheatingStamp.model.User;
import com.example.CheatingStamp.model.UserRole;
import com.example.CheatingStamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String SUPERVISOR_TOKEN = "1234";

    public void registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        // 회원 email 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("해당 email로 가입 내역이 존재합니다.");
        }

        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (requestDto.isSupervisor()) {
            if (!requestDto.getSupervisorToken().equals(SUPERVISOR_TOKEN)) {
                throw new IllegalArgumentException("감독관 암호가 맞지 않습니다.");
            }
            role = UserRole.SUPERVISOR;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
    }

    @Transactional
    public User updateCalibrationRate(User user, CalibrationRateRequestDto requestDto) {
        user.updateCalibrationRate(requestDto);
        return user;
    }

    public Long getFirstExamId(User user) {
        List<Exam> exams = user.getExams();
        if (exams.size() == 0) {  // 예정된 시험이 없을 경우
            return -1L;
        }
        Long firstExamId = exams.get(0).getId();
        LocalDateTime firstExamStartTime = exams.get(0).getStartTime();
        for (int i = 1; i < exams.size(); i++) {
            Exam exam = exams.get(i);
            // 가장 가까운 시험 id 갱신
            if (exam.getStartTime().compareTo(firstExamStartTime) < 0)
                firstExamId = exam.getId();
        }

        return firstExamId;
    }
}