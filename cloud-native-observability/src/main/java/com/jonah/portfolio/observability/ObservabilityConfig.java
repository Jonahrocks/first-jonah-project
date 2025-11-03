package com.jonah.portfolio.observability;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.exporter.SpanExportingPredicate;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfig {

    @Bean
    ObservationRegistryCustomizer<ObservationRegistry> alwaysSample() {
        return registry -> registry.observationConfig()
                .observationPredicate((name, context) -> true);
    }

    @Bean
    SpanExportingPredicate serviceOnlyPredicate() {
        return spanData -> spanData.getName().startsWith("weather");
    }

    @Bean
    OtlpGrpcSpanExporter otlpExporter() {
        return OtlpGrpcSpanExporter.builder().build();
    }
}
