package com.example.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Slf4j {

    companion object {
        internal val <reified T> T.logger: Logger
            inline get() = LoggerFactory.getLogger(T::class.java)

    }

}
