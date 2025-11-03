package com.jonah.portfolio.observability;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.observation.ObservationRegistry;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class WeatherServiceTest {

    @Test
    void shouldEmitWeatherReading() {
        WeatherService service = new WeatherService(ObservationRegistry.create());
        StepVerifier.create(service.fetchReading("London"))
                .assertNext(reading -> {
                    assertThat(reading.location()).isEqualTo("London");
                    assertThat(reading.temperatureCelsius()).isBetween(5.0, 35.0);
                })
                .verifyComplete();
    }
}
