package com.example.retrofit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

object RetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }
    val api: ApiService by lazy { retrofit.create(ApiService::class.java) }

    // SSE流式事件
    fun sseEvents(): Flow<Message> = flow {
        val call = api.getEvents()
        val response = call.execute()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val reader = BufferedReader(InputStreamReader(body.byteStream()))
                reader.useLines { lines ->
                    lines.forEach { line ->
                        if (line.startsWith("data: ")) {
                            val json = line.substring(6) // 移除 "data: " 前缀
                            emit(kotlinx.serialization.json.Json.decodeFromString(Message.serializer(), json))
                        }
                    }
                }
            }
        }
    }
} 