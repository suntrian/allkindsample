package com.example.retrofit

import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Message(val id: Long, val content: String)

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }
    routing {
        // 普通HTTP接口
        get("/hello") {
            println("Received request to /hello")
            call.respond(Message(1, "Hello, World!"))
        }
        
        // SSE接口
        get("/events") {
            println("Received request to /events")

            call.response.header(HttpHeaders.ContentType, ContentType.Text.EventStream.toString())
            call.response.header(HttpHeaders.CacheControl, CacheControl.NoCache(null).toString())
            call.response.header(HttpHeaders.Connection, "keep-alive")
            
            repeat(5) {
                val message = Message(it.toLong(), "Event $it")
                val json = Json.encodeToString(Message.serializer(), message)
                call.respondText("data: $json\n\n", ContentType.Text.EventStream)
                delay(500)
            }
        }
    }
}

fun main() {
    println("Starting server on http://localhost:8080")
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
} 