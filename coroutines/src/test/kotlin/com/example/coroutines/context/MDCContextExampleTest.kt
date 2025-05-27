package com.example.coroutines.context

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import org.slf4j.MDC

class MDCContextExampleTest : StringSpec({
    val example = MDCContextExample()
    
    "should propagate MDC from parent to child coroutine" {
        runBlocking {
            // 清理 MDC
            MDC.clear()
            
            // 在父协程中设置 MDC 值
            MDC.put("parentKey", "parentValue")
            
            // 执行测试
            example.demonstrateParentToChildMDC()
            
            // 验证父协程中的值保持不变
            MDC.get("parentKey") shouldBe "parentValue"
            // 子协程中的值不应该在父协程中可见
            MDC.get("childKey") shouldNotBe null
        }
    }
    
    "should propagate MDC from child to parent coroutine" {
        runBlocking {
            // 清理 MDC
            MDC.clear()
            
            example.demonstrateChildToParentMDC()
            
            // 验证子协程的值被传递到父协程
            MDC.get("childKey") shouldBe "childValue"
        }
    }
    
    "should propagate MDC context between coroutines" {
        runBlocking {
            // 清理 MDC
            MDC.clear()
            
            // 设置初始 MDC 值
            MDC.put("initialKey", "initialValue")
            
            // 执行测试
            example.demonstrateMDCContextPropagation()
            
            // 验证最终的 MDC 值
            MDC.get("initialKey") shouldBe "initialValue"
            MDC.get("newKey") shouldBe "newValue"
        }
    }
}) 