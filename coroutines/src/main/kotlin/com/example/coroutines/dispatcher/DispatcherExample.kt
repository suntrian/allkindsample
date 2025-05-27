package com.example.coroutines.dispatcher

import kotlinx.coroutines.*
import mu.KotlinLogging
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

private val logger = KotlinLogging.logger {}

/**
 * 协程调度器示例
 */
class DispatcherExample {
    private val threadCounter = AtomicInteger(0)
    
    /**
     * 创建自定义调度器，每次恢复都在不同线程执行
     */
    private val rotatingDispatcher = Executors.newCachedThreadPool { r ->
        Thread(r, "RotatingThread-${threadCounter.incrementAndGet()}")
    }.asCoroutineDispatcher()
    
    /**
     * 演示不同调度器的特性
     */
    suspend fun demonstrateDispatchers() = coroutineScope {
        // 1. Default 调度器
        logger.info { "=== Default Dispatcher ===" }
        withContext(Dispatchers.Default) {
            logger.info { "Default dispatcher thread: ${Thread.currentThread().name}" }
            delay(100)
            logger.info { "After delay in Default: ${Thread.currentThread().name}" }
        }
        
        // 2. IO 调度器
        logger.info { "\n=== IO Dispatcher ===" }
        withContext(Dispatchers.IO) {
            logger.info { "IO dispatcher thread: ${Thread.currentThread().name}" }
            delay(100)
            logger.info { "After delay in IO: ${Thread.currentThread().name}" }
        }
        
        // 3. Unconfined 调度器
        logger.info { "\n=== Unconfined Dispatcher ===" }
        withContext(Dispatchers.Unconfined) {
            logger.info { "Unconfined dispatcher thread: ${Thread.currentThread().name}" }
            delay(100)
            logger.info { "After delay in Unconfined: ${Thread.currentThread().name}" }
        }
        
        // 4. 自定义旋转调度器
        logger.info { "\n=== Rotating Dispatcher ===" }
        withContext(rotatingDispatcher) {
            logger.info { "Rotating dispatcher thread 1: ${Thread.currentThread().name}" }
            delay(100)
            logger.info { "After first delay: ${Thread.currentThread().name}" }
            delay(100)
            logger.info { "After second delay: ${Thread.currentThread().name}" }
        }
        
        // 5. 并发任务使用不同调度器
        logger.info { "\n=== Concurrent Tasks with Different Dispatchers ===" }
        val results = listOf(
            async(Dispatchers.Default) {
                logger.info { "Task in Default: ${Thread.currentThread().name}" }
                delay(100)
                "Default Result"
            },
            async(Dispatchers.IO) {
                logger.info { "Task in IO: ${Thread.currentThread().name}" }
                delay(100)
                "IO Result"
            },
            async(rotatingDispatcher) {
                logger.info { "Task in Rotating: ${Thread.currentThread().name}" }
                delay(100)
                "Rotating Result"
            }
        ).awaitAll()
        
        logger.info { "Results: $results" }
    }
    
    /**
     * 演示调度器的线程池特性
     */
    suspend fun demonstrateThreadPoolCharacteristics() = coroutineScope {
        // 1. Default 调度器的线程池特性
        logger.info { "=== Default Dispatcher Thread Pool ===" }
        val defaultJobs = List(10) { index ->
            async(Dispatchers.Default) {
                logger.info { "Default task $index: ${Thread.currentThread().name}" }
                delay(100)
            }
        }
        defaultJobs.awaitAll()
        
        // 2. IO 调度器的线程池特性
        logger.info { "\n=== IO Dispatcher Thread Pool ===" }
        val ioJobs = List(20) { index ->
            async(Dispatchers.IO) {
                logger.info { "IO task $index: ${Thread.currentThread().name}" }
                delay(100)
            }
        }
        ioJobs.awaitAll()
        
        // 3. 自定义旋转调度器的线程池特性
        logger.info { "\n=== Rotating Dispatcher Thread Pool ===" }
        val rotatingJobs = List(5) { index ->
            async(rotatingDispatcher) {
                logger.info { "Rotating task $index: ${Thread.currentThread().name}" }
                delay(100)
                logger.info { "Rotating task $index after delay: ${Thread.currentThread().name}" }
            }
        }
        rotatingJobs.awaitAll()
    }
    
    fun shutdown() {
        rotatingDispatcher.close()
    }
}

fun main() = runBlocking {
    val example = DispatcherExample()
    try {
        example.demonstrateDispatchers()
        println("\n=== Thread Pool Characteristics ===")
        example.demonstrateThreadPoolCharacteristics()
    } finally {
        example.shutdown()
    }
} 