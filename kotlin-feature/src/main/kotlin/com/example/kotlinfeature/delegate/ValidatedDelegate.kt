package com.example.kotlinfeature.delegate

import kotlin.reflect.KProperty
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * 验证委托 - 在设置值时进行验证
 */
class ValidatedDelegate<T>(
    private var value: T,
    private val validator: (T) -> Boolean,
    private val errorMessage: String = "Invalid value"
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        if (!validator(newValue)) {
            throw IllegalArgumentException("$errorMessage: $newValue for property ${property.name}")
        }
        value = newValue
    }
}

/**
 * 扩展函数
 */
fun <T> validated(
    initialValue: T,
    errorMessage: String = "Invalid value",
    validator: (T) -> Boolean
): ValidatedDelegate<T> {
    return ValidatedDelegate(initialValue, validator, errorMessage)
}

/**
 * 使用示例
 */
class ValidatedExample {
    var username: String by validated("", "Username must be at least 3 characters") { 
        it.length >= 3 
    }
    
    var age: Int by validated(0, "Age must be between 0 and 150") { 
        it in 0..150 
    }
    
    var email: String by validated("", "Invalid email format") { 
        it.contains("@") && it.contains(".") 
    }
    
    var password: String by validated("", "Password must be at least 8 characters") { 
        it.length >= 8 
    }
} 