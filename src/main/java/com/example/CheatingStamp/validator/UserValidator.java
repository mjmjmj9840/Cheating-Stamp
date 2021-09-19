package com.example.CheatingStamp.validator;

import com.example.CheatingStamp.dto.SignupRequestDto;
import com.example.CheatingStamp.model.User;
import com.example.CheatingStamp.repository.UserRepository;
import com.example.CheatingStamp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

        @Autowired
        private final UserRepository userRepository;

        @Override
        public boolean supports(Class<?> clazz) {
            return SignupRequestDto.class.equals(clazz);
        }

        @Override
        public void validate(Object obj, Errors errors) {   // 회원가입 화면에서 사용
            SignupRequestDto signupRequestDto = (SignupRequestDto) obj;
            if(!signupRequestDto.getPassword().equals(signupRequestDto.getPassword_check())){
                //비밀번호와 비밀번호 확인이 다르다면
                errors.rejectValue("password", "key","비밀번호가 일치하지 않습니다.");
            }

            Optional<User> found = userRepository.findByUsername(signupRequestDto.getUsername());
            if(found.isPresent()){
                // 이름이 존재하면
                errors.rejectValue("username", "key","이미 사용자 이름이 존재합니다.");
            }
        }

        public boolean isSupervisor(UserDetailsImpl userDetails) {
            if (userDetails.getUser().getRole().name() == "SUPERVISOR") {
                return true;
            }
            else {
                return false;
            }
        }
}