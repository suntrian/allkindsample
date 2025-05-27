package org.example.utils

import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlinx.coroutines.*
import java.util.UUID

@Serializable
class Printer(val message: String) {
    fun printMessage() = runBlocking {
        val now: Instant = Clock.System.now()
        launch {
            delay(1000L)
            println(now.toString())
        }
        println(message)
    }


    fun uuid() {
        print(UUID.randomUUID().toString())
    }


    companion object {


        @JvmStatic
        fun main(vararg args: String): Unit {
            Printer("").uuid()
        }

    }
}