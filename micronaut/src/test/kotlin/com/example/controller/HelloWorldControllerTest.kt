package com.example.controller

import com.example.beans.HelloClient
import com.example.utils.Slf4j
import com.example.utils.Slf4j.Companion.logger
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import reactor.core.publisher.Mono

@Slf4j
@MicronautTest(transactional = false)
class HelloWorldControllerTest {

    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var helloClient: HelloClient

    @ParameterizedTest
    @CsvSource(value = ["world", "nihao"])
    fun testHelloWorldResponse(word: String) {
        val resp = httpClient.toBlocking().retrieve("/hello/$word")
        logger.info("resp: {}", resp)
        Assertions.assertEquals("Hello $word", resp)
    }

    @ParameterizedTest
    @CsvSource(value = ["world", "nihao"])
    fun testHelloClient(word: String) {
        val resp = helloClient.helloWorld(word)
        logger.info("resp: {}", resp)
        Assertions.assertEquals("Hello $word", resp)
    }

    @Test
    fun testHelloJsonResponse() {
        val resp = httpClient.toBlocking().retrieve("/hello/json")
        logger.info("resp: {}", resp)
        Assertions.assertEquals("""{"hello":"world"}""", resp)
    }

    @Test
    fun testHelloJsonReactive() {
        val resp = httpClient.retrieve("/hello/json")
        val respStr = Mono.from(resp).block()
        Assertions.assertEquals("""{"hello":"world"}""", respStr)
    }


    @Test
    fun testHelloClient() {
        val resp = Mono.from(helloClient.helloJson()).block()
        Assertions.assertEquals(mapOf("hello" to "world"), resp)
    }


}

