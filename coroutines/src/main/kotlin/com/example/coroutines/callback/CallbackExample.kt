package com.example.coroutines.callback

import kotlinx.coroutines.*
import mu.KotlinLogging
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val logger = KotlinLogging.logger {}

/**
 * 回调函数和挂起函数转换示例
 */
class CallbackExample {
    private val client = OkHttpClient.Builder()
        .followRedirects(true)
        .build()
    
    /**
     * 将回调函数转换为挂起函数
     */
    suspend fun fetchWithCallback(url: String): String = suspendCoroutine { continuation ->
        val request = Request.Builder()
            .url(url)
            .build()
            
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }
            
            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string() ?: throw IOException("Empty response")
                    continuation.resume(body)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        })
    }
    
    /**
     * 将挂起函数转换为回调函数
     */
    fun fetchWithSuspend(url: String, callback: (Result<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = fetchWithCallback(url)
                callback(Result.success(result))
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }
    
    /**
     * 使用 CompletableFuture 的示例
     */
    fun fetchWithFuture(url: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        
        val request = Request.Builder()
            .url(url)
            .build()
            
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                future.completeExceptionally(e)
            }
            
            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string() ?: throw IOException("Empty response")
                    future.complete(body)
                } catch (e: Exception) {
                    future.completeExceptionally(e)
                }
            }
        })
        
        return future
    }
    
    /**
     * 将 CompletableFuture 转换为挂起函数
     */
    suspend fun fetchWithSuspendFuture(url: String): String = suspendCoroutine { continuation ->
        fetchWithFuture(url)
            .whenComplete { result, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else {
                    continuation.resume(result)
                }
            }
    }
    
    /**
     * 演示各种转换方式
     */
    suspend fun demonstrateConversions() = coroutineScope {
        // 1. 使用回调函数
        logger.info { "=== Using Callback ===" }
        fetchWithSuspend("http://httpbin.org/get") { result ->
            result.fold(
                onSuccess = { logger.info { "Callback success: $it" } },
                onFailure = { logger.error(it) { "Callback error" } }
            )
        }
        
        // 2. 使用挂起函数
        logger.info { "\n=== Using Suspend Function ===" }
        try {
            val result = fetchWithCallback("http://httpbin.org/get")
            logger.info { "Suspend success: $result" }
        } catch (e: Exception) {
            logger.error(e) { "Suspend error" }
        }
        
        // 3. 使用 CompletableFuture
        logger.info { "\n=== Using CompletableFuture ===" }
        fetchWithFuture("http://httpbin.org/get")
            .thenAccept { logger.info { "Future success: $it" } }
            .exceptionally { 
                logger.error(it) { "Future error" }
                null
            }
        
        // 4. 使用挂起函数包装的 CompletableFuture
        logger.info { "\n=== Using Suspend Future ===" }
        try {
            val result = fetchWithSuspendFuture("http://httpbin.org/get")
            logger.info { "Suspend Future success: $result" }
        } catch (e: Exception) {
            logger.error(e) { "Suspend Future error" }
        }
    }
}

fun main() = runBlocking {
    val example = CallbackExample()
    example.demonstrateConversions()
} 