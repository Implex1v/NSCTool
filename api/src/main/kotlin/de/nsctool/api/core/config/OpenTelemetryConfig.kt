package de.nsctool.api.core.config

import io.opentelemetry.exporter.zipkin.ZipkinSpanExporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenTelemetryConfig {
    @Bean
    fun exporter(): ZipkinSpanExporter = ZipkinSpanExporter
        .builder()
        .setEndpoint("http://tempo.observability.svc.cluster.local:9411")
        .build()
}