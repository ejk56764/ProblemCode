package com.example.board.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.board.domain.repository.BoardRepository;
import com.example.board.dto.BoardDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BoardService {
    private BoardRepository boardRepository;

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }
}
