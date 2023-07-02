package com.example.controller

import com.example.utils.Slf4j
import com.example.utils.Slf4j.Companion.logger
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import jakarta.inject.Inject
import org.reactivestreams.Publisher
import java.util.function.Predicate


@Slf4j
@ServerWebSocket("/ws/chat/{topic}/{username}")
class AsyncWebSocketController {

    @Inject
    private lateinit var broadcaster: WebSocketBroadcaster

    @OnOpen
    fun onOpen(topic: String, username: String, session: WebSocketSession): Publisher<String> {
        log("onOpen", session, username, topic)
        if (topic.equals("all")) {
            return broadcaster.broadcast(String.format("[%s] Now making announcements!", username), isValid(topic));
        }
        return broadcaster.broadcast(String.format("[%s] Joined %s!", username, topic), isValid(topic));
    }

    @OnMessage // <5>
    fun onMessage(
        topic: String,
        username: String,
        message: String,
        session: WebSocketSession
    ): Publisher<String> {
        log("onMessage", session, username, topic)
        return broadcaster.broadcast(String.format("[%s] %s", username, message), isValid(topic))
    }

    @OnClose // <6>
    fun onClose(
        topic: String,
        username: String,
        session: WebSocketSession
    ): Publisher<String> {
        log("onClose", session, username, topic)
        return broadcaster.broadcast(String.format("[%s] Leaving %s!", username, topic), isValid(topic))
    }

    private fun log(event: String, session: WebSocketSession, username: String, topic: String) {
        logger.info(
            "* WebSocket: {} received for session {} from '{}' regarding '{}'",
            event,
            session.id,
            username,
            topic
        )
    }

    private fun isValid(topic: String): Predicate<WebSocketSession> { // <7>
        return Predicate<WebSocketSession> { s ->
            topic == "all" || "all" == s.getUriVariables().get(
                "topic",
                String::class.java, null
            ) || topic.equals(s.getUriVariables().get("topic", String::class.java, null), ignoreCase = true)
        } //intra-topic chat
    }
}