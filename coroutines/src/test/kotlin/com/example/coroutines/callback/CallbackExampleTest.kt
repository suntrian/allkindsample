package com.example.coroutines.callback

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking

class CallbackExampleTest : StringSpec({
    val example = CallbackExample()
    
    "should convert between callbacks and suspend functions" {
        runBlocking {
            // 测试回调函数转挂起函数
            val result1 = example.fetchWithCallback("http://httpbin.org/get")
            result1 shouldNotBe null
            
            // 测试挂起函数转回调函数
            var callbackResult: Result<String>? = null
            example.fetchWithSuspend("http://httpbin.org/get") { result ->
                callbackResult = result
            }
            // 等待回调完成
            Thread.sleep(2000)
            callbackResult shouldNotBe null
            
            // 测试 CompletableFuture 转挂起函数
            val result2 = example.fetchWithSuspendFuture("http://httpbin.org/get")
            result2 shouldNotBe null
        }
    }
}) 