package org.example.webfluxSample2.controller;

import org.example.webfluxSample2.service.HelloWorldHandler;
import org.example.webfluxSample2.service.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class Router {
    @Autowired
    private HelloWorldHandler helloWorldHandler;
    @Autowired
    private UserHandler userHandler;

    @Bean
    public RouterFunction<?> routerFunction(){
        return RouterFunctions.route(RequestPredicates.GET("/hello"), helloWorldHandler::helloWorld)
                .andRoute(RequestPredicates.POST("/register"), userHandler::register)
                .andRoute(RequestPredicates.POST("/login"), userHandler::login);
    }
}
