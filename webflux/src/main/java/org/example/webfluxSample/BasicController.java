package org.example.webfluxSample;

import org.example.model.Car;
import org.example.model.Person;
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
