package com.example.coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import mu.KotlinLogging
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

private val logger = KotlinLogging.logger {}

/**
 * Flow 示例
 */
class FlowExample {
    private val threadCounter = AtomicInteger(0)
    
    /**
     * 创建自定义调度器
     */
    private val customDispatcher = Executors.newFixedThreadPool(4) { r: Runnable ->
        Thread(r, "FlowThread-${threadCounter.incrementAndGet()}")
    }.asCoroutineDispatcher()
    
    /**
     * 基本 Flow 操作
     */
    fun basicFlow(): Flow<Int> = flow {
        for (i in 1..5) {
            delay(100)
            emit(i)
        }
    }
    
    /**
     * 使用 Flow 构建器
     */
    fun flowBuilders(): Flow<Int> = flowOf(1, 2, 3, 4, 5)
    
    /**
     * 使用 asFlow 转换
     */
    fun asFlowExample(): Flow<Int> = (1..5).asFlow()
    
    /**
     * 使用 channelFlow
     */
    fun channelFlowExample(): Flow<Int> = channelFlow {
        for (i in 1..5) {
            delay(100)
            send(i)
        }
    }
    
    /**
     * 使用 callbackFlow
     */
    fun callbackFlowExample(): Flow<Int> = callbackFlow {
        for (i in 1..5) {
            delay(100)
            send(i)
        }
        close()
    }
    
    /**
     * 使用 stateFlow
     */
    private val _stateFlow = MutableStateFlow(0)
    val stateFlow: StateFlow<Int> = _stateFlow.asStateFlow()
    
    /**
     * 使用 sharedFlow
     */
    private val _sharedFlow = MutableSharedFlow<Int>()
    val sharedFlow: SharedFlow<Int> = _sharedFlow.asSharedFlow()
    
    /**
     * 演示 Flow 操作符
     */
    suspend fun demonstrateOperators() = coroutineScope {
        // 1. 基本操作符
        logger.info { "=== Basic Operators ===" }
        basicFlow()
            .map { it * 2 }
            .filter { it > 5 }
            .collect { logger.info { "Basic operator result: $it" } }
        
        // 2. 转换操作符
        logger.info { "\n=== Transform Operators ===" }
        basicFlow()
            .transform { emit("Number $it") }
            .collect { logger.info { "Transform result: $it" } }
        
        // 3. 组合操作符
        logger.info { "\n=== Combine Operators ===" }
        val flow1 = flowOf(1, 2, 3)
        val flow2 = flowOf("A", "B", "C")
        
        flow1.zip(flow2) { number, letter ->
            "$number$letter"
        }.collect { logger.info { "Zip result: $it" } }
        
        // 4. 背压处理
        logger.info { "\n=== Backpressure Handling ===" }
        basicFlow()
            .buffer()
            .collect { logger.info { "Buffered result: $it" } }
        
        // 5. 异常处理
        logger.info { "\n=== Exception Handling ===" }
        flow {
            emit(1)
            throw RuntimeException("Error")
        }.catch { e ->
            logger.error(e) { "Caught exception" }
            emit(-1)
        }.collect { logger.info { "Exception handling result: $it" } }
    }
    
    /**
     * 演示 StateFlow 和 SharedFlow
     */
    suspend fun demonstrateStateAndSharedFlow() = coroutineScope {
        // 1. StateFlow
        logger.info { "=== StateFlow ===" }
        val stateJob = launch {
            stateFlow.collect { value ->
                logger.info { "StateFlow value: $value" }
            }
        }
        
        // 更新 StateFlow
        repeat(3) { i ->
            _stateFlow.value = i + 1
            delay(100)
        }
        
        // 2. SharedFlow
        logger.info { "\n=== SharedFlow ===" }
        val sharedJob = launch {
            sharedFlow.collect { value ->
                logger.info { "SharedFlow value: $value" }
            }
        }
        
        // 发送值到 SharedFlow
        repeat(3) { i ->
            _sharedFlow.emit(i + 1)
            delay(100)
        }
        
        // 等待收集完成
        delay(100)
        stateJob.cancel()
        sharedJob.cancel()
    }
    
    /**
     * 演示 Flow 上下文
     */
    suspend fun demonstrateFlowContext() = coroutineScope {
        logger.info { "=== Flow Context ===" }
        
        // 1. 使用 flowOn
        basicFlow()
            .flowOn(Dispatchers.IO)
            .collect { logger.info { "FlowOn result: $it" } }
        
        // 2. 使用 buffer
        basicFlow()
            .buffer()
            .flowOn(customDispatcher)
            .collect { logger.info { "Buffered result: $it" } }
        
        // 3. 使用 conflate
        basicFlow()
            .conflate()
            .flowOn(Dispatchers.Default)
            .collect { logger.info { "Conflated result: $it" } }
    }
    
    fun shutdown() {
        customDispatcher.close()
    }
}

fun main() = runBlocking {
    val example = FlowExample()
    try {
        example.demonstrateOperators()
        println("\n=== State and Shared Flow ===")
        example.demonstrateStateAndSharedFlow()
        println("\n=== Flow Context ===")
        example.demonstrateFlowContext()
    } finally {
        example.shutdown()
    }
} 