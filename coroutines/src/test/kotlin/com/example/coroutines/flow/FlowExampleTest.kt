package com.example.coroutines.flow

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FlowExampleTest : StringSpec({
    val example = FlowExample()
    
    "should demonstrate basic flow operations" {
        runBlocking {
            // 测试基本 Flow
            val result = example.basicFlow().toList()
            result.size shouldBe 5
            
            // 测试 Flow 构建器
            val builderResult = example.flowBuilders().toList()
            builderResult shouldBe listOf(1, 2, 3, 4, 5)
            
            // 测试 asFlow
            val asFlowResult = example.asFlowExample().toList()
            asFlowResult shouldBe listOf(1, 2, 3, 4, 5)
            
            // 测试 channelFlow
            val channelResult = example.channelFlowExample().toList()
            channelResult.size shouldBe 5
            
            // 测试 callbackFlow
            val callbackResult = example.callbackFlowExample().toList()
            callbackResult.size shouldBe 5
        }
    }
    
    "should demonstrate state and shared flow" {
        runBlocking {
            // 测试 StateFlow
            val stateValue = example.stateFlow.first()
            stateValue shouldBe 0
            
            // 测试 SharedFlow
            val sharedValues = mutableListOf<Int>()
            coroutineScope {
                val job = launch {
                    example.sharedFlow.collect { sharedValues.add(it) }
                }
                
                // 等待收集完成
                kotlinx.coroutines.delay(100)
                job.cancel()
            }
            
            // 验证收集到的值
            sharedValues.size shouldBe 0
        }
    }
    
    "should demonstrate flow operators" {
        runBlocking {
            // 测试 map 和 filter
            val result = example.basicFlow()
                .map { it * 2 }
                .filter { it > 5 }
                .toList()
            
            result.all { it > 5 } shouldBe true
            
            // 测试 transform
            val transformResult = example.basicFlow()
                .transform { emit("Number $it") }
                .toList()
            
            transformResult.all { it.startsWith("Number") } shouldBe true
        }
    }
}) 