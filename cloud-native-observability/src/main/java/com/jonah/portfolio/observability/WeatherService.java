package com.jonah.portfolio.observability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import java.time.Instant;
import java.util.Random;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final ObservationRegistry registry;
    private final Random random = new Random();

    public WeatherService(ObservationRegistry registry) {
        this.registry = registry;
    }

    public Mono<WeatherReading> fetchReading(String location) {
        return Observation.createNotStarted("weather.fetch", registry)
                .contextualName("weather")
                .lowCardinalityKeyValue("location", location)
                .observe(() -> Mono.fromSupplier(() -> new WeatherReading(
                        location,
                        round(random.nextDouble(30) + 5),
                        round(random.nextDouble(60) + 20),
                        Instant.now()
                )));
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
