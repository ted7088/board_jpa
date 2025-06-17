package com.example.board_jpa.controller;

import com.example.board_jpa.dto.MemberJoinRequestDto;
import com.example.board_jpa.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberJoinRequestDto", new MemberJoinRequestDto());
        return "member/join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute MemberJoinRequestDto dto,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            memberService.join(dto);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("joinError", e.getMessage());
            return "member/join";
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }
} 