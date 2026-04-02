package com.weather.temperature.controller;

import com.weather.temperature.model.TemperatureDTO;
import com.weather.temperature.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    // 0000086233 Estación por defecto = Villarrica
    // 0000086218 Aeropuerto Internacional Silvio Pettirossi (Luque)
    // 0000086217 Centro Meteorológico Nacional (Asunción)

    @GetMapping(value = "/current-temperature", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TemperatureDTO> getTemperatureByCode(
            @RequestParam(name = "code", defaultValue = "0000086233") String code) {

        TemperatureDTO temperatureDTO = weatherService.getTemperatureByCode(code);

        if (temperatureDTO == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(temperatureDTO);
    }
}
