package com.example.com.example.opentelemetry.config

import io.opentelemetry.sdk.trace.IdGenerator

class ContextIdGenerator: IdGenerator {



    override fun generateSpanId(): String? {
        return IdGenerator.random().generateSpanId()
    }

    override fun generateTraceId(): String? {
        return IdGenerator.random().generateTraceId()
    }


}