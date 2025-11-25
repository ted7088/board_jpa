package com.example.board_jpa.service;

import com.example.board_jpa.domain.Board;
import com.example.board_jpa.dto.BoardRequestDto;
import com.example.board_jpa.dto.BoardResponseDto;
import com.example.board_jpa.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시글 생성
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = new Board();
        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setAuthor(requestDto.getAuthor());

        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.from(savedBoard);
    }

    // 게시글 조회
    @Transactional
    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id: " + id));

        boardRepository.increaseViewCount(id);
        return BoardResponseDto.from(board);
    }

    // 게시글 목록 조회 (정렬 기능 추가)
    public Page<BoardResponseDto> getBoards(String sortBy, Pageable pageable) {
        Sort sort;

        switch (sortBy.toLowerCase()) {
            case "likes":
                sort = Sort.by(Sort.Order.desc("likeCount"), Sort.Order.desc("createdAt"));
                break;
            case "views":
                sort = Sort.by(Sort.Order.desc("viewCount"), Sort.Order.desc("createdAt"));
                break;
            case "latest":
            default:
                sort = Sort.by(Sort.Order.desc("createdAt"));
                break;
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return boardRepository.findAll(sortedPageable).map(BoardResponseDto::from);
    }

    // 게시글 수정
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id: " + id));

        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());

        return BoardResponseDto.from(board);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다. id: " + id);
        }
        boardRepository.deleteById(id);
    }

    // 제목으로 게시글 검색 (정렬 기능 추가)
    public Page<BoardResponseDto> searchBoardsByTitle(String title, String sortBy, Pageable pageable) {
        Sort sort;

        switch (sortBy.toLowerCase()) {
            case "likes":
                sort = Sort.by(Sort.Order.desc("likeCount"), Sort.Order.desc("createdAt"));
                break;
            case "views":
                sort = Sort.by(Sort.Order.desc("viewCount"), Sort.Order.desc("createdAt"));
                break;
            case "latest":
            default:
                sort = Sort.by(Sort.Order.desc("createdAt"));
                break;
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return boardRepository.findByTitleContaining(title, sortedPageable).map(BoardResponseDto::from);
    }

    // 게시글 좋아요
    @Transactional
    public BoardResponseDto likeBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id: " + id));

        boardRepository.increaseLikeCount(id);
        return BoardResponseDto.from(board);
    }

}

    
                        
                        
                        
                        
                
                
                





    