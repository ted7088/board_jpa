package com.example.board_jpa.repository;

import com.example.board_jpa.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 제목으로 게시글 검색
    List<Board> findByTitleContaining(String title);
    
    // 작성자로 게시글 검색
    List<Board> findByAuthor(String author);
    
    // 조회수 증가
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void increaseViewCount(@Param("id") Long id);
} 