package com.example.opentelemetry

import io.opentelemetry.sdk.common.CompletableResultCode
import io.opentelemetry.sdk.trace.data.SpanData
import io.opentelemetry.sdk.trace.export.SpanExporter
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class LoggingSpanExporter : SpanExporter {

    override fun export(spans: Collection<SpanData>): CompletableResultCode {
        spans.forEach { span ->
            logger.info {
                """
                |Span: ${span.name}
                |  Trace ID: ${span.traceId}
                |  Span ID: ${span.spanId}
                |  Parent Span ID: ${span.parentSpanId}
                |  Start Time: ${span.startEpochNanos}
                |  End Time: ${span.endEpochNanos}
                |  Attributes: ${span.attributes}
                |  Events: ${span.events}
                |  Status: ${span.status}
                |  Resource: ${span.resource}
                """.trimMargin()
            }
        }
        return CompletableResultCode.ofSuccess()
    }

    override fun flush(): CompletableResultCode = CompletableResultCode.ofSuccess()

    override fun shutdown(): CompletableResultCode = CompletableResultCode.ofSuccess()
} 