package com.example.rsocketserver;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ExampleController {

    private Map<Object, RSocketRequester> CLIENTS = new HashMap<>();

    @ConnectMapping
    public void connect(RSocketRequester requester, @Payload String clientName){
        requester.rsocket()
                .onClose(

                )
                .doFirst(() -> {
                    System.out.println( clientName + "  ::  ConnectMapping  Connect");
                    CLIENTS.put(clientName,requester);
                })
                .doFinally(consumer -> {
                    System.out.println(clientName + "  ::  ConnectMapping  DisConnect");
                    CLIENTS.remove(clientName);
                })
                .subscribe();
    }

    @MessageMapping("request-response")
    public Mono<String> requestResponse(@Payload String payload){
        System.out.println("request-response : "+ payload);
        return Mono.just("request-response : "+ payload);
    }

    @MessageMapping("fire-and-forget")
    public Mono<Void> fireAndForget(@Payload String payload){
        System.out.println("fire-and-forget : "+ payload);
        return Mono.empty();
    }

    @MessageMapping("request-stream")
    public Flux<String> requestStream(@Payload String payload){
        return Flux.interval(Duration.ofSeconds(1))
                .take(5)
                .map( i -> {
                    System.out.println("stream["+ i+ "]  :: " + payload);
                    return "stream["+ i+ "]  :: " + payload;
                });
    }

    @MessageMapping("channel")
    public Flux<String> channel(@Payload Flux<String> payloads){
        return payloads
                .doOnNext(payload ->
                        System.out.println("channel ( " + payload+" )")
                )
                .map(payload ->
                         "channel ( " + payload+" )"
                );
    }

}
