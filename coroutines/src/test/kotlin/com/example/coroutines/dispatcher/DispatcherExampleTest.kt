package com.example.coroutines.dispatcher

import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.runBlocking

class DispatcherExampleTest : StringSpec({
    val example = DispatcherExample()
    
    "should demonstrate different dispatchers" {
        runBlocking {
            example.demonstrateDispatchers()
        }
    }
    
    "should demonstrate thread pool characteristics" {
        runBlocking {
            example.demonstrateThreadPoolCharacteristics()
        }
    }
}) 