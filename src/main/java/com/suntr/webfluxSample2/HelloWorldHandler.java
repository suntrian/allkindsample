package com.suntr.webfluxSample2;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service("helloWorldHandler")
public class HelloWorldHandler {

  public Mono<ServerResponse> helloWorld(ServerRequest serverRequest) {
    return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
            .body(BodyInserters.fromObject("hello world"));
  }

}
