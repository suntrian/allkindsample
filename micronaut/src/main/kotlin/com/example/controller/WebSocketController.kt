package com.example.controller

import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.*
import jakarta.inject.Inject
import org.reactivestreams.Publisher
import java.util.function.Predicate

@ServerWebSocket("/chat/{topic}/{username}")
class WebSocketController {

    @Inject
    private lateinit var broadcaster: WebSocketBroadcaster

    @OnOpen
    fun onOpen(topic: String, username: String, session: WebSocketSession) {
        broadcaster.broadcastSync("welcome $username to $topic", isValid(topic, session))
    }

    @OnMessage
    fun onMessage(topic: String, username: String, message: String, session: WebSocketSession) {
        val msg = "$username say $message"
        broadcaster.broadcastSync(msg, isValid(topic, session))
    }

    @OnClose
    fun onClose(topic: String, username: String, session: WebSocketSession) {
        val msg = "$username quited $topic"
        broadcaster.broadcastSync(msg, isValid(topic, session))
    }

    @OnError
    fun onError(topic: String, username: String, exception: Exception, session: WebSocketSession): Publisher<String> {
        val msg = "$username failed by ${exception.stackTraceToString()}"
        return broadcaster.broadcast(msg, isValid(topic, session))
    }

    private fun isValid(topic: String, session: WebSocketSession): Predicate<WebSocketSession> {
        return Predicate { /*it !== session &&*/ topic.equals(
            it.uriVariables.get("topic", String::class.java, null),
            ignoreCase = true
        )
        }
    }
}