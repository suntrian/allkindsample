package com.suntr.webfluxFunctionalSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

//@SpringBootApplication
public class SpringBootWebFluxFunctionalSample {
  public static void main(String[] args) {
    SpringApplication.run(SpringBootWebFluxFunctionalSample.class);
  }

  //@Bean
  //@Autowired
//  public RouterFunction<ServerResponse> routerFunction(final CalculatorHandler calculatorHandler){
//    return RouterFunctions.route(RequestPredicates.path("/calculator"), request ->
//            request.queryParam("operator").map(operator ->
//                    Mono.justOrEmpty(ReflectionUtils.findMethod(CalculatorHandler.class, operator, ServerRequest.class))
//                            .flatMap(method -> (Mono<ServerResponse>) ReflectionUtils.invokeMethod(method, calculatorHandler, request))
//                            .switchIfEmpty(ServerResponse.badRequest().build())
//                            .onErrorResume(ex -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
//                    .orElse(ServerResponse.badRequest().build()));
//  }
}
