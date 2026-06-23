package com.weather.temperature.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.temperature.model.TemperatureDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {
    private static final String URL = "https://www.meteorologia.gov.py/emas/data.json";
    private final ConcurrentHashMap<String, TemperatureDTO> temperatureCache = new ConcurrentHashMap<>();
    private static final String DEFAULT_CODE = "0000086233";

    @PostConstruct
    public void init() {
        fetchTemperature(DEFAULT_CODE);
    }

    public TemperatureDTO fetchTemperature(String codigo) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String jsonResponse = restTemplate.getForObject(URL, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode stationNode = rootNode.path("estaciones").path(codigo);
            if (stationNode.isMissingNode() || stationNode.path("ultima_observacion").path("temperatura_aire").isMissingNode()) {
                return null;
            }

            double temperaturaAire = stationNode
                    .path("ultima_observacion")
                    .path("temperatura_aire")
                    .path("valor")
                    .asDouble();

            String valor = String.format(Locale.US, "%.1f", temperaturaAire) + "°";

            TemperatureDTO temperature = new TemperatureDTO(valor);
            temperatureCache.put(codigo, temperature);

        } catch (Exception e) {
            System.out.println("No se pudo actualizar la temperatura para código " + codigo + ". Se usará caché si existe.");
            return temperatureCache.get(codigo);
        }
        return temperatureCache.get(codigo);
    }

    public TemperatureDTO getTemperatureByCode(String codigo) {
        TemperatureDTO temperature = temperatureCache.get(codigo);
        if (temperature == null) {
            return fetchTemperature(codigo);
        }
        return temperature;
    }

    public Iterable<String> getAllCachedCodes() {
        return temperatureCache.keySet();
    }
}
