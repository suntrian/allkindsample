package com.example.grpc

import com.example.grpc.*
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.*
import java.util.concurrent.CancellationException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ChatServer(private val port: Int) {
    private val server: Server = ServerBuilder.forPort(port)
        .addService(ChatServiceImpl())
        .build()

    private val activeClients = ConcurrentHashMap<String, StreamObserver<ChatMessage>>()

    @Throws(IOException::class)
    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(Thread {
            println("*** shutting down gRPC server since JVM is shutting down")
            this@ChatServer.stop()
            println("*** server shut down")
        })
    }

    @Throws(InterruptedException::class)
    fun stop() {
        server.shutdown().awaitTermination(30, TimeUnit.SECONDS)
    }

    @Throws(InterruptedException::class)
    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    private inner class ChatServiceImpl : ChatServiceGrpcKt.ChatServiceCoroutineImplBase() {
        override fun chat(requests: Flow<ChatMessage>): Flow<ChatMessage> = flow {
            val clientId = UUID.randomUUID().toString()
            activeClients[clientId] = object : StreamObserver<ChatMessage> {
                override fun onNext(value: ChatMessage) {
                    println("${clientId}: ${value.content}")
                }
                override fun onError(t: Throwable) {
                    println("$clientId error: ${t.message}")
                    activeClients.remove(clientId)
                }
                override fun onCompleted() {
                    println("$clientId complete")
                    activeClients.remove(clientId)
                }
            }

            // 发送欢迎消息
            val welcomeMsg = ChatMessage.newBuilder()
                .setMessageId(UUID.randomUUID().toString())
                .setContent("hello")
                .setSender("server")
                .setReceiver(clientId)
                .setSendTime(System.currentTimeMillis())
                .build()
            emit(welcomeMsg)

            try {// 处理客户端消息
                requests.collect { request ->
                    val reversedContent = request.content.reversed()
                    val response = ChatMessage.newBuilder()
                        .setMessageId(UUID.randomUUID().toString())
                        .setContent(reversedContent)
                        .setSender("server")
                        .setReceiver(clientId)
                        .setSendTime(System.currentTimeMillis())
                        .setReceiveTime(request.sendTime)
                        .build()
                    emit(response)
                }
            } catch (e: CancellationException){
                println("client quit")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

fun main() {
    val latch = CountDownLatch(1)
    val server = ChatServer(50051)

    try {
        server.start()
        latch.await()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}