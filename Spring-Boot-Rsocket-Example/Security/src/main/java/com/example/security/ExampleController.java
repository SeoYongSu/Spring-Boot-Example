package com.example.security;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.Instant;


@Controller
public class ExampleController {


    @MessageMapping("example")
    public Mono<String> example(String request){
        System.out.println("Example");
        return Mono.just(request + " : " + Instant.now());
    }

}
