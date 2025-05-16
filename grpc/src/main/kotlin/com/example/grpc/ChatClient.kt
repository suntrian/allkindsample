package com.example.grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.*
import java.util.concurrent.TimeUnit

class ChatClient(private val host: String, private val port: Int) {
    private val channel: ManagedChannel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build()
    private val stub: ChatServiceGrpcKt.ChatServiceCoroutineStub = ChatServiceGrpcKt.ChatServiceCoroutineStub(channel)
    private val messagesFlow = MutableSharedFlow<ChatMessage>()
    private var isRunning = true
    val clientId = UUID.randomUUID().toString()

    suspend fun run() {
        val chatJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                // 创建并启动响应收集协程
                val responseJob = launch {
                    val responseFlow = stub.chat(messagesFlow)
                    responseFlow.collect { message ->
                        println("收到消息 [${message.messageId}] (${formatTime(message.sendTime)}): ${message.content}")
                    }
                }

                // 创建并启动用户输入协程
                val inputJob = launch(Dispatchers.IO) {
                    while (isActive && isRunning) {
                        print("> ")
                        val input = readLine() ?: continue
                        
                        if (input == "\\q") {
                            println("退出聊天...")
                            isRunning = false
                            break
                        }
                        
                        val message = ChatMessage.newBuilder()
                            .setMessageId(UUID.randomUUID().toString())
                            .setContent(input)
                            .setSender(clientId)
                            .setReceiver("server")
                            .setSendTime(System.currentTimeMillis())
                            .build()
                        messagesFlow.emit(message)
                    }
                }

                // 等待任意一个协程完成
                inputJob.join()
                responseJob.cancelAndJoin() // 取消响应收集协程
            } catch (e: Exception) {
                println("聊天发生错误: ${e.message}")
            }
        }
        
        chatJob.join() // 等待聊天任务完成
    }

    private fun formatTime(timestamp: Long): String {
        return java.time.Instant.ofEpochMilli(timestamp).toString()
    }

    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun main() {
    val client = ChatClient("localhost", 50051)
    try {
        println("聊天客户端[${client.clientId}]已启动，输入 \\q 退出")
        client.run()
    } finally {
        client.shutdown()
    }
}