package com.suntr.webfluxSample.controller;

import com.suntr.exception.ResourceNotFoundException;
import com.suntr.webfluxSample.entity.User;
import com.suntr.webfluxSample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService){
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public void notFound(){}

    @GetMapping("")
    public Flux<User> listUsers(){
        return this.userService.listUser();
    }

    @GetMapping("/{id}")
    public Mono<User> getUser(@PathVariable("id") String id){
        return this.userService.getById(id);
    }

    @PostMapping("")
    public Mono<User> createUser(@RequestBody final User user){
        return this.userService.createOrUpdate(user);
    }

    @PutMapping("/{id}")
    public Mono<User> updateUser(@PathVariable("id") String id, @RequestBody final User user){
        Objects.requireNonNull(user);
        user.setId(id);
        return this.userService.createOrUpdate(user);
    }

    @DeleteMapping("/{id}")
    public Mono<User> deleteUser(@PathVariable("id") String id){
        return this.userService.delete(id);
    }

    @RequestMapping("/randomInt")
    public Flux<ServerSentEvent<Integer>> randomInt(){
        return this.userService.randomInteger();
    }
}
