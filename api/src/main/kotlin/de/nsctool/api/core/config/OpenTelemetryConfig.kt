package de.nsctool.api.core.config

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenTelemetryConfig {
    @Value("\${tracing.host}")
    private lateinit var otlpHost: String

    @Bean
    fun exporter(): OtlpHttpSpanExporter = OtlpHttpSpanExporter
        .builder()
        .setEndpoint("$otlpHost/v1/traces")
        .build()
}