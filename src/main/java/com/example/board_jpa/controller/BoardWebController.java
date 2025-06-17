package com.example.board_jpa.controller;

import com.example.board_jpa.dto.BoardRequestDto;
import com.example.board_jpa.dto.BoardResponseDto;
import com.example.board_jpa.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardWebController {

    private final BoardService boardService;

    // 게시글 목록 페이지
    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("boards", boardService.getAllBoards());
        return "board/list";
    }

    // 게시글 작성 페이지
    @GetMapping("/board/write")
    public String writeForm(Model model) {
        model.addAttribute("boardRequestDto", new BoardRequestDto());
        return "board/write";
    }

    // 게시글 작성 처리
    @PostMapping("/board/write")
    public String write(@ModelAttribute BoardRequestDto boardRequestDto) {
        boardService.createBoard(boardRequestDto);
        return "redirect:/";
    }

    // 게시글 상세 페이지
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Long id, Model model) {
        BoardResponseDto board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/detail";
    }

    // 게시글 수정 페이지
    @GetMapping("/board/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        BoardResponseDto board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/edit";
    }

    // 게시글 수정 처리
    @PostMapping("/board/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute BoardRequestDto boardRequestDto) {
        boardService.updateBoard(id, boardRequestDto);
        return "redirect:/board/" + id;
    }

    // 게시글 삭제
    @GetMapping("/board/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return "redirect:/";
    }
} 