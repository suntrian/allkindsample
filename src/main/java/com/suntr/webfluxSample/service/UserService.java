package com.suntr.webfluxSample.service;

import com.suntr.exception.ResourceNotFoundException;
import com.suntr.webfluxSample.entity.User;
import io.netty.util.internal.ThreadLocalRandom;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
  private final Map<String, User> users = new ConcurrentHashMap<>();

  public Flux<User> listUser() {
    return Flux.fromIterable(this.users.values());
  }

  public Flux<User> getById(final Flux<String> ids) {
    return ids.flatMap(id-> Mono.justOrEmpty(this.users.get(id)));
  }

  public Mono<User> getById(final String id) {
    return Mono.justOrEmpty(this.users.get(id)).switchIfEmpty(Mono.error(new ResourceNotFoundException("user not found")));
  }

  public Mono<User> createOrUpdate(final User user){
    this.users.put(user.getId(), user);
    return Mono.just(user);
  }

  public Mono<User> delete(final String id) {
    return Mono.justOrEmpty(this.users.remove(id));
  }

  public Flux<ServerSentEvent<Integer>> randomInteger(){
    return Flux.interval(Duration.ofSeconds(1))
            .map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt()))
            .map(data -> ServerSentEvent.<Integer>builder()
                    .event("random")
                    .id(Long.toString(data.getT1()))
                    .data(data.getT2())
                    .build());
  }
}
