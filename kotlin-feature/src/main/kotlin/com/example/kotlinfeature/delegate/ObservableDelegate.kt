package com.example.kotlinfeature.delegate

import kotlin.reflect.KProperty
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * 观察者委托 - 监听属性变化
 */
class ObservableDelegate<T>(
    private var value: T,
    private val onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        val oldValue = value
        value = newValue
        onChange(property, oldValue, newValue)
    }
}

/**
 * 扩展函数
 */
fun <T> observable(
    initialValue: T,
    onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
): ObservableDelegate<T> {
    return ObservableDelegate(initialValue, onChange)
}

/**
 * 使用示例
 */
class ObservableExample {
    var name: String by observable("Unknown") { property, oldValue, newValue ->
        logger.info { "Property ${property.name} changed from '$oldValue' to '$newValue'" }
    }
    
    var age: Int by observable(0) { property, oldValue, newValue ->
        logger.info { "Age changed from $oldValue to $newValue" }
        if (newValue < 0) {
            throw IllegalArgumentException("Age cannot be negative")
        }
    }
    
    var email: String by observable("") { property, oldValue, newValue ->
        logger.info { "Email changed from '$oldValue' to '$newValue'" }
        if (!newValue.contains("@")) {
            throw IllegalArgumentException("Invalid email format")
        }
    }
} 