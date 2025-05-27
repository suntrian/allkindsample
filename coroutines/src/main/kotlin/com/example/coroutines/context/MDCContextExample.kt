package com.example.coroutines.context

import kotlinx.coroutines.*
import mu.KotlinLogging
import org.slf4j.MDC
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

/**
 * 自定义协程上下文元素，用于传递 MDC 上下文
 */
class MDCContext(private val contextMap: Map<String, String>) : AbstractCoroutineContextElement(MDCContext) {
    companion object Key : CoroutineContext.Key<MDCContext>
}

/**
 * MDC 上下文传播示例
 */
class MDCContextExample {
    
    /**
     * 在父协程中设置 MDC 值，并在子协程中获取
     */
    suspend fun demonstrateParentToChildMDC() = coroutineScope {
        // 在父协程中设置 MDC 值
        MDC.put("parentKey", "parentValue")
        logger.info { "Parent coroutine: MDC value = ${MDC.get("parentKey")}" }
        
        // 创建子协程并传递 MDC 上下文
        val childContext = coroutineContext + MDCContext(MDC.getCopyOfContextMap() ?: emptyMap())
        
        withContext(childContext) {
            // 在子协程中获取父协程的 MDC 值
            val parentValue = MDC.get("parentKey")
            logger.info { "Child coroutine: MDC value = $parentValue" }
            
            // 在子协程中修改 MDC 值
            MDC.put("childKey", "childValue")
            logger.info { "Child coroutine: New MDC value = ${MDC.get("childKey")}" }
        }
        
        // 父协程中无法看到子协程设置的 MDC 值
        logger.info { "Parent coroutine: Child MDC value = ${MDC.get("childKey")}" }
    }
    
    /**
     * 在子协程中设置 MDC 值，并在父协程中获取
     */
    suspend fun demonstrateChildToParentMDC() = coroutineScope {
        val parentContext = coroutineContext
        
        // 创建子协程
        val childJob = async {
            // 在子协程中设置 MDC 值
            MDC.put("childKey", "childValue")
            logger.info { "Child coroutine: MDC value = ${MDC.get("childKey")}" }
            
            // 返回 MDC 上下文
            MDC.getCopyOfContextMap()
        }
        
        // 等待子协程完成并获取 MDC 上下文
        val childMDC = childJob.await()
        
        // 在父协程中设置子协程的 MDC 值
        childMDC?.forEach { (key, value) ->
            MDC.put(key, value)
        }
        
        logger.info { "Parent coroutine: Child MDC value = ${MDC.get("childKey")}" }
    }
    
    /**
     * 使用 MDCContext 在协程间传递上下文
     */
    suspend fun demonstrateMDCContextPropagation() = coroutineScope {
        // 创建初始 MDC 上下文
        val initialContext = MDCContext(MDC.getCopyOfContextMap() ?: emptyMap())
        
        // 使用 MDCContext 启动协程
        withContext(initialContext) {
            logger.info { "Coroutine 1: MDC value = ${MDC.get("initialKey")}" }
            
            // 修改 MDC 值
            MDC.put("newKey", "newValue")
            
            // 创建新的 MDCContext
            val newContext = MDCContext(MDC.getCopyOfContextMap() ?: emptyMap())
            
            // 在新的上下文中启动子协程
            withContext(newContext) {
                logger.info { "Coroutine 2: MDC values = ${MDC.get("initialKey")}, ${MDC.get("newKey")}" }
            }
            
            // 确保 MDC 值在父协程中保持
            logger.info { "Coroutine 1 after child: MDC values = ${MDC.get("initialKey")}, ${MDC.get("newKey")}" }
        }
    }
}

fun main() = runBlocking {
    val example = MDCContextExample()
    
    println("=== Parent to Child MDC Propagation ===")
    example.demonstrateParentToChildMDC()
    
    println("\n=== Child to Parent MDC Propagation ===")
    example.demonstrateChildToParentMDC()
    
    println("\n=== MDC Context Propagation ===")
    example.demonstrateMDCContextPropagation()
} 