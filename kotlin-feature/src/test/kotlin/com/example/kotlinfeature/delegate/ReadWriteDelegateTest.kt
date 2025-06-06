package com.example.kotlinfeature.delegate

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ReadWriteDelegateTest {
    
    @Test
    fun `should handle read and write operations`() {
        val example = ReadWriteExample()
        
        // 初始值检查
        assertEquals("Initial Value", example.readWriteProperty)
        assertEquals(0, example.readWriteNumber)
        assertEquals(emptyList<String>(), example.readWriteList)
        
        // 修改值
        example.readWriteProperty = "New Value"
        example.readWriteNumber = 42
        example.readWriteList.add("new item")
        
        // 验证修改后的值
        assertEquals("New Value", example.readWriteProperty)
        assertEquals(42, example.readWriteNumber)
        assertEquals(mutableListOf("new item"), example.readWriteList)
    }
} 