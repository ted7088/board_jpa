package com.example.board_jpa.controller;

import com.example.board_jpa.dto.BoardRequestDto;
import com.example.board_jpa.dto.BoardResponseDto;
import com.example.board_jpa.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardWebController {

    private final BoardService boardService;

    // 게시글 목록 페이지
    @GetMapping("/")
    public String list(Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "latest") String sortBy,
            @RequestParam(value = "searchTitle", required = false) String searchTitle) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<BoardResponseDto> boardList;

        if (searchTitle != null && !searchTitle.isEmpty()) {
            boardList = boardService.searchBoardsByTitle(searchTitle, sortBy, pageable);
        } else {
            boardList = boardService.getBoards(sortBy, pageable);
        }

        int totalPages = boardList.getTotalPages();
        int startPage = 0;
        int endPage = 0;
        int prevGroupStartPage = 0;
        int nextGroupStartPage = 0;
        boolean hasPrevGroup = false;
        boolean hasNextGroup = false;

        if (totalPages > 0) {
            // Fixed block calculation (0-9, 10-19, 20-29, etc.)
            int currentBlock = page / 10;
            startPage = currentBlock * 10;
            endPage = Math.min(totalPages - 1, startPage + 9);

            // Calculate jump targets for Prev/Next buttons
            hasPrevGroup = startPage > 0;
            hasNextGroup = endPage < totalPages - 1;
            prevGroupStartPage = Math.max(0, startPage - 10);
            nextGroupStartPage = startPage + 10;
        }

        model.addAttribute("boards", boardList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", boardList.getTotalElements());
        model.addAttribute("hasNext", hasNextGroup);
        model.addAttribute("hasPrev", hasPrevGroup);
        model.addAttribute("prevGroupStartPage", prevGroupStartPage);
        model.addAttribute("nextGroupStartPage", nextGroupStartPage);
        model.addAttribute("searchTitle", searchTitle);
        model.addAttribute("sortBy", sortBy);

        return "board/list";
    }

    // 게시글 작성 페이지
    @GetMapping("/board/write")
    public String writeForm(Model model) {
        model.addAttribute("boardRequestDto", new BoardRequestDto());
        return "board/write";
    }

    // 게시글 작성 처리 페이지
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