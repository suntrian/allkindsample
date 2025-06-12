package com.example.airbyte

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AirbyteSdkApplication

fun main(args: Array<String>) {
    runApplication<AirbyteSdkApplication>(*args)
} 