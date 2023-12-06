package com.example.graphqlapi;

import com.example.graphqlapi.model.Board;
import com.example.graphqlapi.model.BoardService;
import com.example.graphqlapi.model.Register;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @QueryMapping
//    @SchemaMapping(typeName = "Query")
    public Board getBoardById(@Argument String id){
        log.info("getBoardById");
        return boardService.getById(id);
    }

    @MutationMapping(value = "insertBoard")
    public Board insertBoardA(@Argument String title, @Argument String content){
        log.info("insertBoard_A");
        return boardService.save(title,content);
    }


    @SchemaMapping(value = "reg")
    public Register mappingExample(Board board){
        log.info("SchemaMapping");
        return new Register(board.getId());
    }


}
