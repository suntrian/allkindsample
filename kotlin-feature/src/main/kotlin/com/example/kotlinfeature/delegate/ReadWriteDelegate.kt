package com.example.kotlinfeature.delegate

import kotlin.reflect.KProperty
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * 可读写属性委托
 */
class ReadWriteDelegate<T>(private var value: T) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        logger.info { "Getting value of ${property.name}: $value" }
        return value
    }
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        logger.info { "Setting value of ${property.name} to: $value" }
        this.value = value
    }
}

/**
 * 使用示例
 */
class ReadWriteExample {
    var readWriteProperty: String by ReadWriteDelegate("Initial Value")
    var readWriteNumber: Int by ReadWriteDelegate(0)
    var readWriteList: MutableList<String> by ReadWriteDelegate(mutableListOf())
} 