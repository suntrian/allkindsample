package com.example.kotlinfeature.delegate

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

class ValidatedDelegateTest {
    
    @Test
    fun `should validate property values`() {
        val example = ValidatedExample()
        
        // 初始值检查
        assertEquals("", example.username)
        assertEquals(0, example.age)
        assertEquals("", example.email)
        assertEquals("", example.password)
        
        // 验证用户名长度
        val usernameException = assertThrows(IllegalArgumentException::class.java) {
            example.username = "ab"
        }
        assertEquals("Username must be at least 3 characters: ab for property username", usernameException.message)
        
        example.username = "valid_username"
        assertEquals("valid_username", example.username)
        
        // 验证年龄范围
        val ageException = assertThrows(IllegalArgumentException::class.java) {
            example.age = 151
        }
        assertEquals("Age must be between 0 and 150: 151 for property age", ageException.message)
        
        example.age = 25
        assertEquals(25, example.age)
        
        // 验证邮箱格式
        val emailException = assertThrows(IllegalArgumentException::class.java) {
            example.email = "invalid-email"
        }
        assertEquals("Invalid email format: invalid-email for property email", emailException.message)
        
        example.email = "valid@email.com"
        assertEquals("valid@email.com", example.email)
        
        // 验证密码长度
        val passwordException = assertThrows(IllegalArgumentException::class.java) {
            example.password = "short"
        }
        assertEquals("Password must be at least 8 characters: short for property password", passwordException.message)
        
        example.password = "valid_password_123"
        assertEquals("valid_password_123", example.password)
    }
} 