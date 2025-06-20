package com.example.board_jpa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequestDto {
    
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;
    
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
} 