package com.example.retrofit

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.serialization.Serializable

class RetrofitClientTest {

    @Test
    fun testHello() = runBlocking {
        println("Testing /hello endpoint with Retrofit")
        val apiService = RetrofitClient.api
        val msg = apiService.getHello()
        assertEquals("Hello, World!", msg.content)
    }

    @Test
    fun testSseEvents() = runBlocking {
        println("Testing /events endpoint with Retrofit")
        val events = RetrofitClient.sseEvents().toList()
        assertEquals(5, events.size)
        assertEquals("Event 0", events[0].content)
    }

    @Test
    fun testHelloWithOkHttp() {
        println("Testing /hello endpoint with OkHttp")
        val client = OkHttpClient.Builder()
            .build()
        
        val request = Request.Builder()
            .url("http://localhost:8080/hello")
            .get()
            .build()

        println("Sending request to: ${request.url}")
        client.newCall(request).execute().use { response ->
            println("Response code: ${response.code}")
            println("Response message: ${response.message}")
            if (!response.isSuccessful) {
                println("Error response body: ${response.body?.string()}")
            }
            assert(response.isSuccessful) { "HTTP request failed: ${response.code}" }
            val responseBody = response.body?.string()
            println("Response body: $responseBody")
            assertNotNull(responseBody) { "Response body is null" }
            
            val message = Json.decodeFromString(Message.serializer(), responseBody)
            assertEquals("Hello, World!", message.content)
        }
    }

    @Test
    fun testSseWithOkHttp() {
        println("Testing /events endpoint with OkHttp")
        val client = OkHttpClient.Builder()
            .build()
        
        val request = Request.Builder()
            .url("http://localhost:8080/events")
            .get()
            .build()

        println("Sending request to: ${request.url}")
        client.newCall(request).execute().use { response ->
            println("Response code: ${response.code}")
            println("Response message: ${response.message}")
            if (!response.isSuccessful) {
                println("Error response body: ${response.body?.string()}")
            }
            assert(response.isSuccessful) { "HTTP request failed: ${response.code}" }
            val responseBody = response.body?.string()
            println("Response body: $responseBody")
            assertNotNull(responseBody) { "Response body is null" }
            
            // 解析SSE响应
            val events = responseBody.split("\n\n")
                .filter { it.startsWith("data: ") }
                .map { 
                    val json = it.substring(6) // 移除 "data: " 前缀
                    Json.decodeFromString(Message.serializer(), json)
                }
            
            assertEquals(5, events.size)
            assertEquals("Event 0", events[0].content)
        }
    }
} 