package com.example.opentelemetry

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanContext
import io.opentelemetry.api.trace.SpanId
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.TraceFlags
import io.opentelemetry.api.trace.TraceId
import io.opentelemetry.api.trace.TraceState
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.IdGenerator
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.SpanProcessor
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.sdk.trace.samplers.Sampler
import kotlinx.coroutines.*
import mu.KotlinLogging
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

class ManualTraceExample {
    private val openTelemetry: OpenTelemetry
    private val tracer: Tracer
    private val spanProcessor: SpanProcessor

    init {
        val resource = Resource.getDefault()
            .merge(Resource.create(Attributes.of(
                AttributeKey.stringKey("service.name"), "manual-trace-example"
            )))

        // 创建 BatchSpanProcessor
        spanProcessor = SimpleSpanProcessor.builder(LoggingSpanExporter()).build()

        val sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(spanProcessor)
            .setSampler(Sampler.alwaysOn())
            .setResource(resource)
            .setIdGenerator(IdGenerator.random())
            .build()

        openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .build()

        tracer = openTelemetry.getTracer("com.example.opentelemetry")
    }

    // 创建自定义的SpanContext
    private fun createSpanContext(traceId: String, spanId: String, parentSpanId: String? = null): SpanContext {
        return SpanContext.create(
            traceId,
            spanId,
            TraceFlags.getSampled(),
            TraceState.builder().put("stop", "true").build()
        )
    }

    // 创建带有自定义traceId和spanId的span
    private fun createSpan(name: String, traceId: String, spanId: String, parentSpanId: String? = null): Span {
        val spanContext = createSpanContext(traceId, spanId, parentSpanId)
        return tracer.spanBuilder(name)
            .setSpanKind(SpanKind.INTERNAL)
            .setParent(Context.current().with(Span.wrap(spanContext)))
            .startSpan()
    }

    suspend fun processWithManualTrace() = coroutineScope {
        // 设置固定的traceId (32个字符的十六进制字符串)
        val traceId = "00000000000000000000000000000001"
        
        // 创建根span (16个字符的十六进制字符串)
        val rootSpan = createSpan("root-operation", traceId, "0000000000000001")
        
        launch(Dispatchers.IO + coroutineContext) {
            try {
                val currentContext = Context.current().with(rootSpan)
                currentContext.makeCurrent()
                
                logger.info { "Starting root operation" }
                rootSpan.setAttribute("operation.type", "root")
                
                // 创建子span
                val childSpan1 = createSpan("child-operation-1", traceId, "0000000000000002", "0000000000000001")
                try {
                    val childContext = Context.current().with(childSpan1)
                    childContext.makeCurrent()
                    
                    logger.info { "Processing child operation 1" }
                    childSpan1.setAttribute("operation.type", "child1")
                    delay(100)
                    
                    // 创建孙span
                    val grandChildSpan = createSpan("grandchild-operation", traceId, "0000000000000003", "0000000000000002")
                    try {
                        val grandChildContext = Context.current().with(grandChildSpan)
                        grandChildContext.makeCurrent()
                        
                        logger.info { "Processing grandchild operation" }
                        grandChildSpan.setAttribute("operation.type", "grandchild")
                        delay(50)
                    } finally {
                        grandChildSpan.end()
                    }
                } finally {
                    childSpan1.end()
                }
                
                // 创建另一个子span
                val childSpan2 = createSpan("child-operation-2", traceId, "0000000000000004", "0000000000000001")
                try {
                    val childContext = Context.current().with(childSpan2)
                    childContext.makeCurrent()
                    
                    logger.info { "Processing child operation 2" }
                    childSpan2.setAttribute("operation.type", "child2")
                    delay(150)
                } finally {
                    childSpan2.end()
                }
            } finally {
                rootSpan.end()
            }
        }
    }

    // 关闭处理器
    fun shutdown() {
        spanProcessor.forceFlush()
        spanProcessor.shutdown()
    }
}

fun mainManualTrace(): Unit = runBlocking {
    val example = ManualTraceExample()
    try {
        example.processWithManualTrace()
    } finally {
        // 确保在程序退出前刷新和关闭处理器
        example.shutdown()
    }
}

fun main() {
    mainManualTrace()
}