package com.example.kotlinfeature.delegate

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ReadOnlyDelegateTest {
    
    @Test
    fun `should return initial value for read-only property`() {
        val example = ReadOnlyExample()
        
        assertEquals("Hello World", example.readOnlyProperty)
        assertEquals(42, example.readOnlyNumber)
        assertEquals(listOf("a", "b", "c"), example.readOnlyList)
    }
} 