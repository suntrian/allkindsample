package com.example

import io.opentelemetry.api.trace.SpanId
import io.opentelemetry.api.trace.TraceId
import java.util.UUID

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    print(TraceId.isValid(UUID.randomUUID().toString().replace("-", "")))



}