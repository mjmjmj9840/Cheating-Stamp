package com.example.CheatingStamp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private boolean supervisor = false;
    private String supervisorToken = "";
}
