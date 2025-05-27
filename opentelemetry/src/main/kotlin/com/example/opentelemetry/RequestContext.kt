package com.example.opentelemetry

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

data class RequestContext(
    val requestId: String,
    val accountId: String
) : AbstractCoroutineContextElement(RequestContext) {
    companion object Key : CoroutineContext.Key<RequestContext> {
        override fun toString(): String = "RequestContext.Key"
    }

    override fun toString(): String {
        return "RequestContext(requestId='$requestId', accountId='$accountId')"
    }
} 