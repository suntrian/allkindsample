package org.example.webfluxSample2.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class RouterFunctionConfiguration {
    @Bean
    public RouterFunction<ServerResponse> webFlux() {
        return RouterFunctions.route(RequestPredicates.GET("/webFlux"), request -> {
            Mono<String> str = Mono.just("Hello World").delayElement(Duration.ofMillis(10));
            return ServerResponse.ok().body(str, String.class);
        });
    }
}