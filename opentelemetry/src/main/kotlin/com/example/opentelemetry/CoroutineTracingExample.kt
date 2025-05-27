package com.example.opentelemetry

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.extension.kotlin.asContextElement
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import kotlinx.coroutines.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class CoroutineTracingExample {
    private val openTelemetry: OpenTelemetry
    private val tracer: Tracer

    init {
        // 配置OpenTelemetry SDK
        val resource = Resource.getDefault()
            .merge(Resource.create(Attributes.of(
                AttributeKey.stringKey("service.name"), "coroutine-tracing-example"
            )))

        val sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter()))
            .setResource(resource)
            .build()

        openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .build()

        tracer = openTelemetry.getTracer("com.example.opentelemetry")
    }

    suspend fun runExample() = coroutineScope {
        // 创建父span
        val parentSpan = tracer.spanBuilder("parent-operation")
            .setSpanKind(SpanKind.INTERNAL)
            .startSpan()

        // 使用父span的context创建新的coroutine context
        val parentContext = Context.current().with(parentSpan).asContextElement()

        // 在父context中启动子协程
        withContext(parentContext) {
            logger.info { "Starting parent operation" }
            
            // 在父span中执行一些操作
            parentSpan.setAttribute("parent.attribute", "value")
            
            // 启动多个子协程，每个都有自己的span
            val jobs = List(3) { index ->
                async {
                    // 创建子span
                    val childSpan = tracer.spanBuilder("child-operation-$index")
                        .setSpanKind(SpanKind.INTERNAL)
                        .startSpan()
                    
                    try {
                        withContext(childSpan.asContextElement()) {
                            logger.info { "Starting child operation $index" }
                            childSpan.setAttribute("child.attribute", "value-$index")
                            
                            // 模拟一些工作
                            delay(100)
                            
                            // 在子span中再启动一个孙协程
                            launch {
                                val grandChildSpan = tracer.spanBuilder("grandchild-operation-$index")
                                    .setSpanKind(SpanKind.INTERNAL)
                                    .startSpan()
                                
                                try {
                                    withContext(grandChildSpan.asContextElement()) {
                                        logger.info { "Starting grandchild operation $index" }
                                        grandChildSpan.setAttribute("grandchild.attribute", "value-$index")
                                        delay(50)
                                    }
                                } finally {
                                    grandChildSpan.end()
                                }
                            }
                        }
                    } finally {
                        childSpan.end()
                    }
                }
            }
            
            // 等待所有子协程完成
            jobs.awaitAll()
        }
        
        // 结束父span
        parentSpan.end()
    }
}

fun main() = runBlocking {
    val example = CoroutineTracingExample()
    example.runExample()
} 