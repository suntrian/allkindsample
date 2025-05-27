package com.example.opentelemetry

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanContext
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.TraceFlags
import io.opentelemetry.api.trace.TraceState
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.extension.kotlin.asContextElement
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import kotlinx.coroutines.*
import mu.KotlinLogging
import java.util.UUID

private val logger = KotlinLogging.logger {}

class RequestTracingExample {
    private val openTelemetry: OpenTelemetry
    private val tracer: Tracer
    private val traceId = "test_trace"

    init {
        val resource = Resource.getDefault()
            .merge(Resource.create(Attributes.of(
                AttributeKey.stringKey("service.name"), "request-tracing-example"
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

    // 修改SpanNode类
    data class SpanNode(
        val name: String,
        val traceId: String,
        val spanId: String,
        val parentSpanId: String?,
        val attributes: Map<String, String>,
        val events: List<String>,
        val children: MutableList<SpanNode> = mutableListOf()
    ) {
        fun addChild(child: SpanNode) {
            children.add(child)
        }

        fun printTree(level: Int = 0) {
            val indent = "  ".repeat(level)
            println("$indent├─ $name")
            println("$indent│  TraceId: $traceId")
            println("$indent│  SpanId: $spanId")
            println("$indent│  ParentSpanId: ${parentSpanId ?: "root"}")
            if (attributes.isNotEmpty()) {
                println("$indent│  Attributes:")
                attributes.forEach { (key, value) ->
                    println("$indent│    $key: $value")
                }
            }
            if (events.isNotEmpty()) {
                println("$indent│  Events:")
                events.forEach { event ->
                    println("$indent│    - $event")
                }
            }
            children.forEach { it.printTree(level + 1) }
        }
    }

    // 添加span收集器
    private val spanCollector = mutableMapOf<String, SpanNode>()

    // 修改span收集方法
    private fun collectSpan(span: Span, name: String, parentSpan: Span? = null) {
        val spanContext = span.spanContext
        val node = SpanNode(
            name = name,
            traceId = spanContext.traceId,
            spanId = spanContext.spanId,
            parentSpanId = parentSpan?.spanContext?.spanId,
            attributes = emptyMap(),
            events = emptyList()
        )
        spanCollector[spanContext.spanId] = node
    }

    // 修改树构建方法
    private fun buildSpanTree(): SpanNode? {
        // 找到根节点（没有父节点的span）
        val rootNode = spanCollector.values.find { it.parentSpanId == null }
        if (rootNode == null) return null

        // 根据实际的父子关系构建树
        spanCollector.values.forEach { node ->
            if (node.parentSpanId != null) {
                // 找到父节点并添加当前节点作为子节点
                spanCollector[node.parentSpanId]?.addChild(node)
            }
        }

        return rootNode
    }

    // 修改processRequest方法
    suspend fun processRequest(requestId: String, accountId: String) = coroutineScope {
        val requestContext = RequestContext(requestId, accountId)
        
        val rootSpan = tracer.spanBuilder("process-request")
            .setSpanKind(SpanKind.INTERNAL)
            .startSpan()

        val combinedContext = coroutineContext + requestContext
        launch(Dispatchers.IO + combinedContext) {
            try {
                val currentContext = Context.current().with(rootSpan)
                currentContext.makeCurrent()
                
                logger.info { "Starting request processing with context: $requestContext" }
                
                rootSpan.setAttribute("request.id", requestId)
                rootSpan.setAttribute("account.id", accountId)
                
                processAuthentication()
                processAuthorization()
                processBusinessLogic()
                processWithErrorHandling()
                processMultiStageOperation()
                processWithTimestamps()

                // 收集根span信息，没有父span
                collectSpan(rootSpan, "process-request")
            } finally {
                rootSpan.end()
                
                // 构建并打印span树
                val spanTree = buildSpanTree()
                println("\nSpan Tree Structure:")
                println("==================")
                spanTree?.printTree()
                println("==================")
            }
        }
    }

    // 修改其他处理方法
    private suspend fun processAuthentication() = coroutineScope {
        val span = tracer.spanBuilder("authentication")
            .setSpanKind(SpanKind.INTERNAL)
            .setParent(Context.current())
            .startSpan()

        launch(Dispatchers.IO + coroutineContext) {
            try {
                val currentContext = Context.current().with(span)
                currentContext.makeCurrent()
                
                logger.info { "Processing authentication" }
                val requestContext = coroutineContext[RequestContext.Key]
                logger.info { "Request context in authentication: $requestContext" }
                
                span.setAttribute("auth.method", "token")
                delay(100)

                val secondSpan = tracer.spanBuilder("authentication-details")
                    .setSpanKind(SpanKind.INTERNAL)
                    .setParent(Context.current())
                    .startSpan()

                try {
                    val secondContext = Context.current().with(secondSpan)
                    secondContext.makeCurrent()
                    logger.info { "Processing authentication details" }
                    delay(50)
                } finally {
                    // 收集子span信息，传入父span
                    collectSpan(secondSpan, "authentication-details", span)
                    secondSpan.end()
                }
            } finally {
                // 收集当前span信息，传入父span
                collectSpan(span, "authentication", Span.current())
                span.end()
            }
        }
    }

    // 修改其他处理方法
    private suspend fun processAuthorization() = coroutineScope {
        val span = tracer.spanBuilder("authorization")
            .setSpanKind(SpanKind.INTERNAL)
            .setParent(Context.current())
            .startSpan()

        launch(Dispatchers.IO + coroutineContext) {
            try {
                val currentContext = Context.current().with(span)
                currentContext.makeCurrent()
                
                logger.info { "Processing authorization" }
                val requestContext = coroutineContext[RequestContext.Key]
                logger.info { "Request context in authorization: $requestContext" }
                
                span.setAttribute("auth.role", "user")
                delay(50)
            } finally {
                collectSpan(span, "authorization", Span.current())
                span.end()
            }
        }
    }

    private suspend fun processBusinessLogic() = coroutineScope {
        val span = tracer.spanBuilder("business-logic")
            .setSpanKind(SpanKind.INTERNAL)
            .setParent(Context.current())
            .startSpan()

        launch(Dispatchers.IO + coroutineContext) {
            try {
                val currentContext = Context.current().with(span)
                currentContext.makeCurrent()
                
                logger.info { "Processing business logic" }
                val requestContext = coroutineContext[RequestContext.Key]
                logger.info { "Request context in business logic: $requestContext" }
                
                span.setAttribute("operation.type", "read")
                recordBusinessOperationEvents()
                delay(200)
            } finally {
                collectSpan(span, "business-logic", Span.current())
                span.end()
            }
        }
    }

    // 记录业务处理事件的方法
    private fun recordBusinessOperationEvents() {
        val span = Span.current()
        span.addEvent("business_operation_started", Attributes.of(
            AttributeKey.stringKey("operation"), "data_processing",
            AttributeKey.longKey("timestamp"), System.currentTimeMillis()
        ))
        
        span.addEvent("business_operation_completed", Attributes.of(
            AttributeKey.stringKey("status"), "success",
            AttributeKey.longKey("processing_time_ms"), 200L
        ))
    }

    // 记录异常处理事件的方法
    private fun recordErrorHandlingEvents(e: Exception? = null) {
        val span = Span.current()
        span.addEvent("error_handling_started")
        
        if (e != null) {
            span.addEvent("operation_failed", Attributes.of(
                AttributeKey.stringKey("error_type"), e.javaClass.simpleName,
                AttributeKey.stringKey("error_message"), e.message ?: "Unknown error"
            ))
            span.recordException(e)
        } else {
            span.addEvent("operation_successful", Attributes.of(
                AttributeKey.stringKey("result"), "success"
            ))
        }
    }

    // 记录多阶段处理事件的方法
    private suspend fun recordMultiStageEvents() {
        val span = Span.current()
        
        // 阶段1：数据准备
        span.addEvent("stage_started", Attributes.of(
            AttributeKey.stringKey("stage"), "data_preparation"
        ))
        delay(100)
        span.addEvent("stage_completed", Attributes.of(
            AttributeKey.stringKey("stage"), "data_preparation",
            AttributeKey.longKey("duration_ms"), 100L
        ))
        
        // 阶段2：数据处理
        span.addEvent("stage_started", Attributes.of(
            AttributeKey.stringKey("stage"), "data_processing"
        ))
        delay(150)
        span.addEvent("stage_completed", Attributes.of(
            AttributeKey.stringKey("stage"), "data_processing",
            AttributeKey.longKey("duration_ms"), 150L
        ))
        
        // 阶段3：结果验证
        span.addEvent("stage_started", Attributes.of(
            AttributeKey.stringKey("stage"), "result_validation"
        ))
        delay(50)
        span.addEvent("stage_completed", Attributes.of(
            AttributeKey.stringKey("stage"), "result_validation",
            AttributeKey.longKey("duration_ms"), 50L
        ))
    }

    // 记录带时间戳的事件的方法
    private fun recordTimestampedEvents() {
        val span = Span.current()
        val startTime = System.currentTimeMillis()
        
        span.addEvent("operation_started", Attributes.of(
            AttributeKey.stringKey("operation"), "timestamped_operation"
        ))
        
        val midTime = System.currentTimeMillis()
        span.addEvent("operation_midpoint", Attributes.of(
            AttributeKey.stringKey("status"), "in_progress",
            AttributeKey.longKey("elapsed_ms"), midTime - startTime
        ))
        
        val endTime = System.currentTimeMillis()
        span.addEvent("operation_completed", Attributes.of(
            AttributeKey.stringKey("status"), "completed",
            AttributeKey.longKey("total_duration_ms"), endTime - startTime
        ))
    }

    private suspend fun processWithErrorHandling() = coroutineScope {
        val span = tracer.spanBuilder("error-handling")
            .setSpanKind(SpanKind.INTERNAL)
            .setParent(Context.current())
            .startSpan()

        launch(Dispatchers.IO + coroutineContext) {
            try {
                val currentContext = Context.current().with(span)
                currentContext.makeCurrent()
                
                try {
                    if (Math.random() > 0.5) {
                        throw RuntimeException("模拟的业务异常")
                    }
                    recordErrorHandlingEvents()
                } catch (e: Exception) {
                    recordErrorHandlingEvents(e)
                }
            } finally {
                collectSpan(span, "error-handling", Span.current())
                span.end()
            }
        }
    }

    private suspend fun processMultiStageOperation() = coroutineScope {
        val span = tracer.spanBuilder("multi-stage-operation")
            .setSpanKind(SpanKind.INTERNAL)
            .setParent(Context.current())
            .startSpan()

        launch(Dispatchers.IO + coroutineContext) {
            try {
                val currentContext = Context.current().with(span)
                currentContext.makeCurrent()
                
                recordMultiStageEvents()
            } finally {
                collectSpan(span, "multi-stage-operation", Span.current())
                span.end()
            }
        }
    }

    private suspend fun processWithTimestamps() = coroutineScope {
        val span = tracer.spanBuilder("timestamped-events")
            .setSpanKind(SpanKind.INTERNAL)
            .setParent(Context.current())
            .startSpan()

        launch(Dispatchers.IO + coroutineContext) {
            try {
                val currentContext = Context.current().with(span)
                currentContext.makeCurrent()
                
                recordTimestampedEvents()
            } finally {
                collectSpan(span, "timestamped-events", Span.current())
                span.end()
            }
        }
    }

    // 修改 mainRequestTracing 方法以包含新的示例
    fun mainRequestTracing() = runBlocking {
        val example = RequestTracingExample()
        // 模拟处理多个请求
        val requests = List(2) { index ->
            async(Dispatchers.Default) {
                val requestId = UUID.randomUUID().toString()
                val accountId = "ACC-${index + 1}"
                example.processRequest(requestId, accountId)
            }
        }
        
        // 添加新的示例调用
        val errorHandling = async(Dispatchers.Default) {
            example.processWithErrorHandling()
        }
        
        val multiStage = async(Dispatchers.Default) {
            example.processMultiStageOperation()
        }
        
        val timestamped = async(Dispatchers.Default) {
            example.processWithTimestamps()
        }
        
        // 等待所有请求处理完成
        requests.awaitAll()
        errorHandling.await()
        multiStage.await()
        timestamped.await()
    }
}

fun main() {
    val example = RequestTracingExample()
    example.mainRequestTracing()
}