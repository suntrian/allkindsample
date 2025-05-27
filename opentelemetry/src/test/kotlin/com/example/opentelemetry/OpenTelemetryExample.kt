package com.example.opentelemetry

import io.opentelemetry.api.GlobalOpenTelemetry
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
import io.opentelemetry.extension.kotlin.getOpenTelemetryContext
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.ReadWriteSpan
import io.opentelemetry.sdk.trace.ReadableSpan
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import mu.withLoggingContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.coroutines.suspendCoroutine
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.time.Duration

class OpenTelemetryExample {

    private val logger = LoggerFactory.getLogger(OpenTelemetryExample::class.java)
    private val openTelemetry: OpenTelemetry
    private val tracer: Tracer

    private val tracerName = "com.example.opentelemetry"

    init {
        val resource = Resource.getDefault()
            .merge(Resource.create(Attributes.of(
                AttributeKey.stringKey("service.name"), "OpenTelemetry-Example"
            )))


        val sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.builder(LoggingSpanExporter()).build())
            .setResource(resource)
            .build()

        openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .build()

        tracer = openTelemetry.getTracer(tracerName)
    }

    @Test
    fun testGetTracer() {
        val tracer2 = openTelemetry.getTracer(tracerName)
        val tracer3 = openTelemetry.getTracer("com.example.opentelmetry.OpenTelemetryExample")
        logger.info("{}", tracer === tracer2)
        logger.info("{}", tracer != tracer3)
        assertEquals(tracer, tracer2)
        assertNotEquals(tracer, tracer3)

        GlobalOpenTelemetry.set(openTelemetry)
        val tracer4 = GlobalOpenTelemetry.getTracer(tracerName)
        assertEquals(tracer, tracer4)

        GlobalOpenTelemetry.resetForTest()
        val tracer5 = GlobalOpenTelemetry.getTracer(tracerName)
        assertNotEquals(tracer, tracer5)

        //make span by different tracer
        val span1 = tracer.spanBuilder("span1")
            .setSpanKind(SpanKind.INTERNAL)
            .startSpan()
        span1.makeCurrent()
        val span1Ctx = Context.current()
        span1.setAttribute("level", 1)
        val span2 = tracer3.spanBuilder("span2")
            .setSpanKind(SpanKind.INTERNAL)
            .startSpan()
        span2.makeCurrent()
        val span2Ctx = Context.current()
        assertNotEquals(span1Ctx, span2Ctx)
        span2.setAttribute("level", 2)
        val span3 = tracer3.spanBuilder("span3")
            .setSpanKind(SpanKind.INTERNAL)
            .setParent(span1Ctx)
            .startSpan()
        val span3Ctx1 = Context.current()
        span3.addEvent("event")
        span3.end()
        val span3Ctx2 = Context.current()
        assertEquals(span3Ctx1, span2Ctx)
        assertEquals(span3Ctx1, span3Ctx2)

        val span4 = tracer4.spanBuilder("span4")
            .setSpanKind(SpanKind.INTERNAL)
            .startSpan()
        span4.setAttribute("level", 3)
        span4.end()
        val span5 = tracer5.spanBuilder("span5")
            .setSpanKind(SpanKind.INTERNAL)
            .startSpan()
        span5.setAttribute("level", 3)
        span5.end()
        span2.end()
        span1.end()
        assertNotEquals(span2.spanContext, span4.spanContext)
        assertEquals(span1.spanContext, (span2 as ReadWriteSpan).parentSpanContext)
        assertEquals(span1.spanContext,  (span3 as ReadWriteSpan).parentSpanContext)
        assertEquals(span2.spanContext, (span4 as ReadWriteSpan).parentSpanContext)
    }

    @Test
    fun testManualSetTraceIdAndSpanId() {
        val spanCtx = SpanContext.create(
            "1".repeat(32),
            "1".repeat(16),
            TraceFlags.getSampled(),
            TraceState.builder().put("state", "open").build()
        )
        val rootSpan = Span.wrap(spanCtx)

        //expect no effect
        rootSpan.setAttribute("type", rootSpan.javaClass.simpleName)
        rootSpan.makeCurrent()
        //expect no effect
        rootSpan.addEvent("in my context")
        val childSpan = tracer.spanBuilder("child")
            .startSpan()
        childSpan.makeCurrent()
        childSpan.setAttribute("type", childSpan.javaClass.simpleName)
        childSpan.addEvent("in child context")
        childSpan.end()
        // rootSpan is PropagatedSpan, no effect on end, will not export
        rootSpan.end()

        val forceModifyRootSpan = tracer.spanBuilder("root")
            .startSpan()
        val spanContext = forceModifyRootSpan.spanContext
        spanContext.traceId
    }

    @Test
    fun spanCase() {
        val rootSpan = tracer.spanBuilder("rootSpan")
            .setSpanKind(SpanKind.SERVER)
            .setAttribute("level", 1)
            .setNoParent()
            .startSpan()
        try {
            rootSpan.makeCurrent()
            val childSpan1 = tracer.spanBuilder("childSpan1")
                .setSpanKind(SpanKind.INTERNAL)
                .setParent(Context.current())
                .setAttribute("level", 2)
                .startSpan()
            logger.info("within child span 1")
            childSpan1.end()

            val childSpan2 = tracer.spanBuilder("childSpan2")
                .setSpanKind(SpanKind.INTERNAL)
                .setAttribute("level", 2)
                .startSpan()
            logger.info("within child span 2")
            childSpan2.end()
        } finally {
            rootSpan.end()
        }
    }

    @Test
    fun suspendCase() = runTest(timeout = Duration.INFINITE){
        val spanCtx = SpanContext.create(
            "1".repeat(32),
            "1".repeat(16),
            TraceFlags.getSampled(),
            TraceState.builder().put("state", "open").build()
        )
        val wrapSpan = Span.wrap(spanCtx)
        logger.info("wrap spanId: {}", wrapSpan.spanContext.spanId)
        val rootCtx = wrapSpan.storeInContext(Context.root())
        rootCtx.makeCurrent()
        val rootSpan = Span.fromContext(rootCtx)
        Assertions.assertSame(wrapSpan, rootSpan)
        rootSpan.makeCurrent()
        logger.info("root spanId: {}", rootSpan.spanContext.spanId)
        launch(Dispatchers.IO) {
            val childSpan1 = tracer.spanBuilder("child in new coroutine")
                .startSpan()
            assertNotEquals(rootSpan.spanContext.spanId, (childSpan1 as ReadableSpan).parentSpanContext.spanId)
            assertEquals(SpanContext.getInvalid().spanId, (childSpan1 as ReadableSpan).parentSpanContext.spanId)
            logger.info("child spanId1: {}", childSpan1.spanContext.spanId)
            childSpan1.makeCurrent()
            childSpan1.setAttribute("thread", Thread.currentThread().name)
            childSpan1.setAttribute("parent", (childSpan1 as ReadableSpan).parentSpanContext.spanId)
            childSpan1.end()
        }.join()
        launch(Dispatchers.IO + Context.current().asContextElement()) {
            val childSpan2 = tracer.spanBuilder("child in trans coroutine")
                .startSpan()
            assertEquals(rootSpan.spanContext.spanId, (childSpan2 as ReadableSpan).parentSpanContext.spanId)
            logger.info("child spanId2: {}", childSpan2.spanContext.spanId)
            val childScope2 = childSpan2.makeCurrent()
            childSpan2.setAttribute("thread", Thread.currentThread().name)
            childSpan2.setAttribute("parent", (childSpan2 as ReadableSpan).parentSpanContext.spanId)

            withContext(Context.current().asContextElement()) {
                val span = tracer.spanBuilder("grand child 21")
                    .startSpan()
                assertEquals(childSpan2.spanContext.spanId, (span as ReadableSpan).parentSpanContext.spanId)
                span.end()
            }
            childScope2.close()
            childSpan2.end()

            withContext(Context.current().asContextElement()) {
                val span = tracer.spanBuilder("child 2")
                    .startSpan()
                assertEquals(rootSpan.spanContext.spanId, (span as ReadableSpan).parentSpanContext.spanId)
                span.end()
            }
        }.join()
        rootSpan.end()
        wrapSpan.end()
    }



}
