package com.example.kotlinfeature.delegate

import kotlin.reflect.KProperty
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * 只读属性委托
 */
class ReadOnlyDelegate<T>(private val value: T) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        logger.info { "Getting value of ${property.name}: $value" }
        return value
    }
}

/**
 * 使用示例
 */
class ReadOnlyExample {
    val readOnlyProperty: String by ReadOnlyDelegate("Hello World")
    val readOnlyNumber: Int by ReadOnlyDelegate(42)
    val readOnlyList: List<String> by ReadOnlyDelegate(listOf("a", "b", "c"))
} 