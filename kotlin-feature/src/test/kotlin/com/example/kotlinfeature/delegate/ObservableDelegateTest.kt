package com.example.kotlinfeature.delegate

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

class ObservableDelegateTest {
    
    @Test
    fun `should notify on property change`() {
        val example = ObservableExample()
        
        // 初始值检查
        assertEquals("Unknown", example.name)
        assertEquals(0, example.age)
        assertEquals("", example.email)
        
        // 修改值
        example.name = "John Doe"
        assertEquals("John Doe", example.name)
        
        // 验证年龄验证
        val ageException = assertThrows(IllegalArgumentException::class.java) {
            example.age = -1
        }
        assertEquals("Age cannot be negative", ageException.message)
        
        // 验证邮箱格式
        val emailException = assertThrows(IllegalArgumentException::class.java) {
            example.email = "invalid-email"
        }
        assertEquals("Invalid email format", emailException.message)
        
        // 设置有效值
        example.email = "valid@email.com"
        assertEquals("valid@email.com", example.email)
    }
} 