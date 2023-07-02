package com.example.controller

import com.example.beans.Vehicle
import com.example.model.SSEBody
import com.example.utils.Slf4j
import com.example.utils.Slf4j.Companion.logger
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.sse.Event
import io.micronaut.runtime.http.scope.RequestScope
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import jakarta.inject.Inject
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.Thread.sleep
import java.util.function.BiFunction
import javax.validation.constraints.Pattern

@RequestScope
@Slf4j
@Controller("/hello")
open class HelloWorldController {

    @Inject
    lateinit var server: EmbeddedServer

    @PostConstruct
    fun initial() {
        logger.info(javaClass.simpleName + " initial")
    }

    @PreDestroy
    fun finalize() {
        logger.info(javaClass.simpleName + " finalized")
    }

    @Get("/{word}")
    @Produces(MediaType.TEXT_PLAIN)
    open fun helloWorld(
        @PathVariable(
            "word",
            defaultValue = "World"
        ) @Pattern(regexp = "[a-zA-Z]+") word: String
    ): String {
        logger.info("hello {}", word)
        return "Hello $word"
    }

    @Get("/json")
    fun helloJson(): Mono<Map<String, Any>> {
        logger.info("hello json")
        return Mono.just(mapOf("hello" to "world"))
    }

    @Get("/vehicle", produces = [MediaType.TEXT_PLAIN])
    fun engineStart(): String {
        val engine = server.applicationContext.getBean(Vehicle::class.java)
        return engine.start()
    }

    @ExecuteOn(TaskExecutors.IO)
    @Get("/sse")
    @Produces(MediaType.TEXT_EVENT_STREAM)
    fun sse(@QueryValue("count", defaultValue = "100") count: Int): Publisher<Event<SSEBody<String>>> {
        return Flux.generate({ 0 }, BiFunction { value, sink ->
            if (value < count) {
                sleep(1000)
                sink.next(Event.of(SSEBody(value, data = value.toString())))
            } else {
                sink.complete()
            }
            return@BiFunction value + 1
        })
    }

    @ExecuteOn(TaskExecutors.IO)
    @Get("stream")
    @Produces(MediaType.APPLICATION_JSON_STREAM)
    fun jsonStream(@QueryValue("count", defaultValue = "100") count: Int): Publisher<SSEBody<String>> {
        return Flux.generate({ 0 }, BiFunction { value, sink ->
            if (value < count) {
                sleep(1000)
                sink.next(SSEBody(value, data = value.toString()))
            } else {
                sink.complete()
            }
            return@BiFunction value + 1
        })
    }

}