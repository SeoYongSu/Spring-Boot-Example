package com.example.security;

import io.rsocket.metadata.WellKnownMimeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rsocket.server.LocalRSocketServerPort;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.rsocket.metadata.BasicAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.rsocket.server.port=0")
class SecurityTest {

    @LocalRSocketServerPort
    int port;
    
    @Test
    void securitySuccess(){
        MimeType authenticationMimeType =
                MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());

        UsernamePasswordMetadata credentials = new UsernamePasswordMetadata("user", "password");

        RSocketRequester requester = RSocketRequester.builder()
                .setupMetadata(credentials, authenticationMimeType)
                .rsocketStrategies(
                        RSocketStrategies.builder().encoder(new SimpleAuthenticationEncoder()).build()
                )
                .connectTcp("localhost", port)
                .block();


        String result = requester.route("example")
                .data("hello security")
                .retrieveMono(String.class)
                .block();



        System.out.println(result);

    }

}
