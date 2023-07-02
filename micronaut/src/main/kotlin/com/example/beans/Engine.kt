package com.example.beans

import com.example.utils.Slf4j.Companion.logger
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import jakarta.inject.Singleton

interface Engine {

    val cylinders: Int

    fun start(): String

}


@Singleton
class V8Engine : Engine {

    @PostConstruct
    fun initial() {
        logger.info(javaClass.simpleName + " initial")
    }

    @PreDestroy
    fun finalize() {
        logger.info(javaClass.simpleName + " finalized")
    }

    override val cylinders: Int = 8

    override fun start(): String = "Starting V8"

}

@Singleton
class Vehicle(private val engine: Engine) {

    @PostConstruct
    fun initial() {
        logger.info(javaClass.simpleName + " initial")
    }

    @PreDestroy
    fun finalize() {
        logger.info(javaClass.simpleName + " finalized")
    }

    fun start(): String {
        return engine.start()
    }
}