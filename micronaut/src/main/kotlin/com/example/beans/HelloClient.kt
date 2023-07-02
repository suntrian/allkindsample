package com.example.beans

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher

@Client("/hello")
interface HelloClient {

    @Get("/{word}", consumes = [MediaType.TEXT_PLAIN])
    @SingleResult
    fun helloWorld(@PathVariable("word") word: String): String

    @Get("/json", consumes = [MediaType.APPLICATION_JSON])
    @SingleResult
    fun helloJson(): Publisher<Map<String, Any>>

}