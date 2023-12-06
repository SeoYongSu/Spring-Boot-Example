package com.example.graphqlapi.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMongoRepository boardMongoRepository;

    public Board save(String title, String content){
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        return boardMongoRepository.save(board);
    }


    public Board getById(String id){
        return boardMongoRepository.findById(id).orElse(null);
    }
}
