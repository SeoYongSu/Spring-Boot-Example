package com.example.rsocketserver;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExampleHandler implements RSocket {
    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        return RSocket.super.fireAndForget(payload);
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        return RSocket.super.requestResponse(payload);
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        return RSocket.super.requestStream(payload);
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return RSocket.super.requestChannel(payloads);
    }
}
