package com.weather.temperature.config;

import com.weather.temperature.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired
    private WeatherService weatherService;

    // 10 minutes is 600000 milliseconds
    // 15 minutes is 900000 milliseconds
    // 30 minutes is 1800000 milliseconds

    @Scheduled(fixedRate = 1800000)
    public void fetchTemperatureAtInterval() {
        for (String code : weatherService.getAllCachedCodes()) {
            weatherService.fetchTemperature(code);
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            System.out.println("Current temperature updated! | time = " + currentTime);
        }
    }
}
