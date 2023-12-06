package com.example.rsocketclient.command;


import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.nio.channels.ClosedChannelException;
import java.util.List;

@ShellComponent
public class ExampleCommand {
    private RSocketRequester requester = RSocketRequester.builder()
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .setupData("First Connect")
//            .setupRoute("ConnectMapping URL ", "SetupData...")
            .tcp("localhost", 7000);


    @ShellMethod("connect")
    public void connect(String connectName){
        requester = RSocketRequester.builder()
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .setupData(connectName)
                .tcp("localhost", 7000);

        System.out.println( connectName +" :: Connect");
    }

    @ShellMethod(key= "disconnect")
    public void disconnect() {
        if(requester.isDisposed()){
            System.out.println("이미 연결 끊겨 있음");
        }else{
            requester.dispose();
            System.out.println("연결 종료");
        }
    }

    @ShellMethod(key = "request-response")
    public void requestResponse(String payload) {
        if(requester.isDisposed()){
            System.out.println("연결이 끊겨 있어요 다시 연결하세요~");
        } else{
            requester.route("request-response")
                    .data(payload)
                    .retrieveMono(String.class)
                    .subscribe(
                            response -> System.out.println(response)
                    );
        }
    }


    @ShellMethod(key = "fire-and-forget")
    public void fireAndForget(String payload) {
        if(requester.isDisposed()){
            System.out.println("연결이 끊겨 있어요 다시 연결하세요~");
        } else {
            requester.route("fire-and-forget")
                    .data(payload)
                    .retrieveMono(Void.class)
                    .subscribe();
        }
    }


    @ShellMethod(key = "request-stream")
    public void requestStream(String payload) {
        if(requester.isDisposed()){
            System.out.println("연결이 끊겨 있어요 다시 연결하세요~");
        } else{
            requester.route("request-stream")
                    .data(payload)
                    .retrieveFlux(String.class)
                    .doOnNext( response ->{
                        System.out.println(response);
                    })
                    .doFinally(signalType -> {
                        System.out.println("stream 종료");
                    })
                    .subscribe();
        }
    }


    @ShellMethod(key = "channel")
    public void channel(List<String> payloads){
        Flux<String> fluxPayloads = Flux.fromIterable(payloads);
        requester.route("channel")
                .data(fluxPayloads)
                .retrieveFlux(String.class)
                .doOnNext( response ->{
                    System.out.println(response);
                })
                .doFinally(signalType -> {
                    System.out.println("channel 종료");
                })
                .subscribe();


    }



}
