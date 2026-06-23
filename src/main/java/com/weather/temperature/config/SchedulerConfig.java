package com.weather.temperature.config;

import com.weather.temperature.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired
    private WeatherService weatherService;

    // 10 minutes is 600000 milliseconds
    // 15 minutes is 900000 milliseconds
    // 30 minutes is 1800000 milliseconds

    @Scheduled(fixedRate = 900000)
    public void fetchTemperatureAtInterval() {
        for (String code : weatherService.getAllCachedCodes()) {
            weatherService.fetchTemperature(code);
            String currentTime = LocalDateTime.now(ZoneId.of("America/Asuncion")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            System.out.println("Current temperature updated! | Local time = " + currentTime + " | Current temperature is = " + weatherService.fetchTemperature(code).getTemperature() + "C");
        }
    }
}
