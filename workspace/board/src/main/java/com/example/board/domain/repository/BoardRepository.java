package com.example.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.domain.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}