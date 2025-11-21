package com.example.board_jpa.repository;

import com.example.board_jpa.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 제목으로 게시글 검색
    Page<Board> findByTitleContaining(String title, Pageable pageable);
    
    // 작성자로 게시글 검색
    Page<Board> findByAuthor(String author, Pageable pageable);
    
    // 조회수 증가
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void increaseViewCount(@Param("id") Long id);
}