package com.example.graphqlclient;

import com.example.graphqlclient.client.GraphqlCallVo;
import com.example.graphqlclient.client.GraphqlClientExample;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final GraphqlClientExample graphqlClientExample;
    @GetMapping("/{id}")
    public GraphqlCallVo client(@PathVariable("id") String id){
        return graphqlClientExample.get(id);
//        return null;
    }
}
