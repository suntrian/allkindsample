package com.suntr.webfluxSample2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello")
public class HelloController {


    @GetMapping("/world")
    public String hello(){
        return "hello WOrld to reactive world";
    }

    @GetMapping("/mono")
    public Mono<String> mono(){
        return Mono.just("Hello Mono");
    }




}
