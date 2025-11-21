package com.example.board_jpa.controller;

import com.example.board_jpa.dto.BoardRequestDto;
import com.example.board_jpa.dto.BoardResponseDto;
import com.example.board_jpa.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@Valid @RequestBody BoardRequestDto requestDto) {
        BoardResponseDto responseDto = boardService.createBoard(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long id) {
        BoardResponseDto responseDto = boardService.getBoard(id);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<Page<BoardResponseDto>> getAllBoards(
            @PageableDefault(size = 10, sort = "id", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<BoardResponseDto> boards = boardService.getBoards(pageable);
        return ResponseEntity.ok(boards);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(
            @PathVariable Long id,
            @Valid @RequestBody BoardRequestDto requestDto) {
        BoardResponseDto responseDto = boardService.updateBoard(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }

    // 제목으로 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<Page<BoardResponseDto>> searchBoards(
            @RequestParam String title,
            @PageableDefault(size = 10, sort = "id", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<BoardResponseDto> boards = boardService.searchBoardsByTitle(title, pageable);
        return ResponseEntity.ok(boards);
    }
}