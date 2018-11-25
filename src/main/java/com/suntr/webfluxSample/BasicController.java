package com.suntr.webfluxSample;

import com.suntr.springSample.model.Car;
import com.suntr.springSample.model.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class BasicController {


    @GetMapping("/hello")
    public Mono<String> sayHello() {
        return Mono.just("Hello World");
    }

    @GetMapping("/person")
    public Mono<Person> getPerson() {
        return Mono.just(new Person("name",5, new Car("car",100)));
    }
}
