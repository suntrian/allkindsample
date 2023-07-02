package com.example.controller

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.util.CollectionUtils
import io.micronaut.http.uri.UriBuilder
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.websocket.WebSocketClient
import io.micronaut.websocket.annotation.ClientWebSocket
import io.micronaut.websocket.annotation.OnMessage
import jakarta.inject.Inject
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import javax.validation.constraints.NotBlank

@Property(name = "spec.name", value = "ChatWebSocketTest") // <1>
@MicronautTest(transactional = false)
internal class ChatServerWebSocketTest {
    @Inject
    var beanContext: BeanContext? = null

    @Inject
    var embeddedServer: EmbeddedServer? = null

    @Requires(property = "spec.name", value = "ChatWebSocketTest") // <1>
    @ClientWebSocket // <3>

    internal abstract class TestWebSocketClient : AutoCloseable {
        // <4>
        private val messageHistory: Deque<String> = ConcurrentLinkedDeque()
        val latestMessage: String
            get() = messageHistory.peekLast()
        val messagesChronologically: List<String>
            get() = ArrayList(messageHistory)

        @OnMessage
        fun  // <5>
                onMessage(message: String) {
            messageHistory.add(message)
        }

        abstract fun send(@NonNull message: @NotBlank String?) // <6>
    }

    private fun createWebSocketClient(port: Int, username: String, topic: String): TestWebSocketClient {
        val webSocketClient = beanContext!!.getBean(
            WebSocketClient::class.java
        )
        val uri = UriBuilder.of("ws://localhost")
            .port(port)
            .path("ws")
            .path("chat")
            .path("{topic}")
            .path("{username}")
            .expand(CollectionUtils.mapOf("topic", topic, "username", username) as MutableMap<String, in Any>)
        val client = webSocketClient.connect(
            TestWebSocketClient::class.java, uri
        ) // <7>
        return Flux.from(client).blockFirst()
    }

    @Test
    @Throws(Exception::class)
    fun testWebsockerServer() {
        val adam = createWebSocketClient(embeddedServer!!.port, "adam", "Cats & Recreation") // <8>
        Awaitility.await().until { listOf("[adam] Joined Cats & Recreation!") == adam.messagesChronologically }
        val anna = createWebSocketClient(embeddedServer!!.port, "anna", "Cats & Recreation")
        Awaitility.await().until { listOf("[anna] Joined Cats & Recreation!") == anna.messagesChronologically }
        Awaitility.await().until {
            mutableListOf(
                "[adam] Joined Cats & Recreation!",
                "[anna] Joined Cats & Recreation!"
            ) == adam.messagesChronologically
        }
        val ben = createWebSocketClient(embeddedServer!!.port, "ben", "Fortran Tips & Tricks")
        Awaitility.await().until { listOf("[ben] Joined Fortran Tips & Tricks!") == ben.messagesChronologically }
        val zach = createWebSocketClient(embeddedServer!!.port, "zach", "all")
        Awaitility.await().until { listOf("[zach] Now making announcements!") == zach.messagesChronologically }
        val cienna = createWebSocketClient(embeddedServer!!.port, "cienna", "Fortran Tips & Tricks")
        Awaitility.await().until { listOf("[cienna] Joined Fortran Tips & Tricks!") == cienna.messagesChronologically }
        Awaitility.await().until {
            mutableListOf(
                "[ben] Joined Fortran Tips & Tricks!",
                "[zach] Now making announcements!",
                "[cienna] Joined Fortran Tips & Tricks!"
            ) == ben.messagesChronologically
        }

        // should broadcast message to all users inside the topic // <11>
        val adamsGreeting = "Hello, everyone. It's another purrrfect day :-)"
        val expectedGreeting = "[adam] $adamsGreeting"
        adam.send(adamsGreeting)

        //subscribed to "Cats & Recreation"
        Awaitility.await().until { expectedGreeting == adam.latestMessage }

        //subscribed to "Cats & Recreation"
        Awaitility.await().until { expectedGreeting == anna.latestMessage }

        //NOT subscribed to "Cats & Recreation"
        Assertions.assertNotEquals(expectedGreeting, ben.latestMessage)

        //subscribed to the special "all" topic
        Awaitility.await().until { expectedGreeting == zach.latestMessage }

        //NOT subscribed to "Cats & Recreation"
        Assertions.assertNotEquals(expectedGreeting, cienna.latestMessage)

        // should broadcast message when user disconnects from the chat // <12>
        anna.close()
        val annaLeaving = "[anna] Leaving Cats & Recreation!"
        Awaitility.await().until { annaLeaving == adam.latestMessage }

        //subscribed to "Cats & Recreation"
        Assertions.assertEquals(annaLeaving, adam.latestMessage)

        //Anna already left and therefore won't see the message about her leaving
        Assertions.assertNotEquals(annaLeaving, anna.latestMessage)

        //NOT subscribed to "Cats & Recreation"
        Assertions.assertNotEquals(annaLeaving, ben.latestMessage)

        //subscribed to the special "all" topic
        Assertions.assertEquals(annaLeaving, zach.latestMessage)

        //NOT subscribed to "Cats & Recreation"
        Assertions.assertNotEquals(annaLeaving, cienna.latestMessage)
        adam.close()
        ben.close()
        zach.close()
        cienna.close()
    }
}
