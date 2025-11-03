package com.jonah.portfolio.observability;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class WeatherController {

    private final WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @GetMapping(value = "/api/weather", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Timed(value = "weather.stream", description = "Time spent streaming weather updates")
    public Flux<WeatherReading> streamReadings(@RequestParam(defaultValue = "London") String location) {
        return Flux.interval(java.time.Duration.ofSeconds(1))
                .flatMap(tick -> service.fetchReading(location))
                .take(5);
    }
}
