package com.example.board_jpa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {
    
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하로 입력해주세요.")
    private String title;
    
    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;
    
    @NotBlank(message = "작성자는 필수 입력 항목입니다.")
    @Size(min = 2, max = 50, message = "작성자는 2자 이상 50자 이하로 입력해주세요.")
    private String author;
} 