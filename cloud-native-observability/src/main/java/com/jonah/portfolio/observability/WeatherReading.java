package com.jonah.portfolio.observability;

import java.time.Instant;

public record WeatherReading(String location, double temperatureCelsius, double humidity, Instant timestamp) {
}
